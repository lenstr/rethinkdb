#include "rdb_protocol/changefeed.hpp"

#include "concurrency/interruptor.hpp"
#include "containers/archive/boost_types.hpp"
#include "rpc/mailbox/typed.hpp"
#include "rdb_protocol/btree.hpp"
#include "rdb_protocol/env.hpp"
#include "rdb_protocol/val.hpp"

#include "debug.hpp"

namespace ql {

namespace changefeed {

// RSI: Does this actually work?
// * When is it OK to invalidate a variable (`this`) captured by the function
//   passed to a mailbox?
server_t::server_t(mailbox_manager_t *_manager)
    : manager(_manager), stop_mailbox(manager, [this](msg_t::addr_t addr) {
            auto_drainer_t::lock_t lock(&drainer);
            rwlock_in_line_t spot(&clients_lock, access_t::read);
            spot.read_signal()->wait_lazily_unordered();
            clients[addr]->pulse();
        }) { }

void server_t::add_client(const msg_t::addr_t &addr) {
    auto_drainer_t::lock_t lock(&drainer);
    rwlock_in_line_t spot(&clients_lock, access_t::write);
    spot.write_signal()->wait_lazily_unordered();
    clients[addr].init(new cond_t());
    signal_t *stopped = &*clients[addr];
    // We spawn now so the auto drainer lock is acquired immediately.
    coro_t::spawn_now_dangerously(
        [this, stopped, addr]() {
            auto_drainer_t::lock_t coro_lock(&drainer);
            disconnect_watcher_t disconnect(manager->get_connectivity_service(),
                                            addr.get_peer());
            {
                wait_any_t wait_any(&disconnect, stopped, coro_lock.get_drain_signal());
                wait_any.wait_lazily_unordered();
            }
            debugf("Stopping...\n");
            rwlock_in_line_t coro_spot(&clients_lock, access_t::write);
            coro_spot.write_signal()->wait_lazily_unordered();
            size_t erased = clients.erase(addr);
            guarantee(erased == 1);
            debugf("Stopped!\n");
        }
    );
}

void server_t::send_all(const msg_t &msg) {
    auto_drainer_t::lock_t lock(&drainer);
    rwlock_in_line_t spot(&clients_lock, access_t::read);
    spot.read_signal()->wait_lazily_unordered();
    for (auto it = clients.begin(); it != clients.end(); ++it) {
        send(manager, it->first, msg);
    }
}

server_t::addr_t server_t::get_stop_addr() {
    return stop_mailbox.get_address();
}

msg_t::msg_t(msg_t &&msg) : op(std::move(msg.op)) { }
msg_t::msg_t(const msg_t &msg) : op(msg.op) { }
msg_t::msg_t(stop_t &&_op) : op(std::move(_op)) { }
msg_t::msg_t(change_t &&_op) : op(std::move(_op)) { }

msg_t::change_t::change_t() { }
msg_t::change_t::change_t(const rdb_modification_report_t *report)
  : old_val(report->info.deleted.first), new_val(report->info.added.first) { }
msg_t::change_t::~change_t() { }

RDB_IMPL_ME_SERIALIZABLE_1(msg_t, op);
RDB_IMPL_ME_SERIALIZABLE_2(msg_t::change_t, empty_ok(old_val), empty_ok(new_val));
RDB_IMPL_ME_SERIALIZABLE_0(msg_t::stop_t);


// Uses the home thread of the subscriber, not the client.
class sub_t : public home_thread_mixin_t {
public:
    // Throws QL exceptions.
    sub_t(feed_t *_feed);
    ~sub_t();
    std::vector<counted_t<const datum_t> >
    get_els(batcher_t *batcher, const signal_t *interruptor);
    void add_el(counted_t<const datum_t> d);
    void abort(const std::string &msg);
private:
    void maybe_signal_cond() THROWS_NOTHING;
    std::exception_ptr exc;
    cond_t *cond; // NULL unless we're waiting.
    std::deque<counted_t<const datum_t> > els;
    feed_t *feed;
    auto_drainer_t drainer;
    DISABLE_COPYING(sub_t);
};

class feed_t : public home_thread_mixin_t, public slow_atomic_countable_t<feed_t> {
public:
    feed_t(client_t *client, base_namespace_repo_t *ns_repo, uuid_u uuid);
    ~feed_t();
    void add_sub(sub_t *sub) THROWS_NOTHING;
    void del_sub(sub_t *sub) THROWS_NOTHING;
    template<class T> void each_sub(const T &t) THROWS_NOTHING;
    bool can_be_removed();
private:
    client_t *client;
    uuid_u uuid;
    mailbox_manager_t *manager;
    mailbox_t<void(msg_t)> mailbox;
    std::vector<server_t::addr_t> stop_addrs;
    std::vector<scoped_ptr_t<disconnect_watcher_t> > disconnect_watchers;
    wait_any_t any_disconnect;
    std::vector<std::set<sub_t *> > subs;
    rwlock_t subs_lock;
    int64_t num_subs;
    bool detached;
    auto_drainer_t drainer;
};

class msg_visitor_t : public boost::static_visitor<void> {
public:
    msg_visitor_t(feed_t *_feed) : feed(_feed) { }
    void operator()(const msg_t::change_t &change) const {
        auto null = make_counted<const datum_t>(datum_t::R_NULL);
        std::map<std::string, counted_t<const datum_t> > obj{
            {"new_val", change.new_val.has() ? change.new_val : null},
            {"old_val", change.old_val.has() ? change.old_val : null}
        };
        auto d = make_counted<const datum_t>(std::move(obj));
        feed->each_sub([d](sub_t *sub) { sub->add_el(d); });
    }
    void operator()(const msg_t::stop_t &) const {
        const char *msg = "Changefeed aborted (table dropped).";
        feed->each_sub([msg](sub_t *sub) { sub->abort(msg); });
    }
private:
    feed_t *feed;
};
void handle_msg(feed_t *feed, const msg_t &msg) {
    boost::apply_visitor(msg_visitor_t(feed), msg.op);
}

class stream_t : public eager_datum_stream_t {
public:
    template<class... Args>
    stream_t(scoped_ptr_t<sub_t> &&_sub, Args... args)
        : eager_datum_stream_t(std::forward<Args...>(args...)),
          sub(std::move(_sub)) { }
    virtual bool is_array() { return false; }
    virtual bool is_exhausted() const { return false; }
    virtual std::vector<counted_t<const datum_t> >
    next_raw_batch(env_t *env, const batchspec_t &bs) {
        batcher_t batcher = bs.to_batcher();
        return sub->get_els(&batcher, env->interruptor);
    }
private:
    scoped_ptr_t<sub_t> sub;
};

sub_t::sub_t(feed_t *_feed) : cond(NULL), feed(_feed) {
    auto_drainer_t::lock_t lock(&drainer);
    feed->add_sub(this);
}

sub_t::~sub_t() {
    debugf("destroy %p\n", this);
    // This error is only sent if we're getting destroyed while blocking.
    abort("Subscription destroyed (shutting down?).");
    debugf("del_sub %p\n", this);
    // RSI: make sure this runs after `feed->add_sub` is *done*.
    feed->del_sub(this);
    debugf("destroyed %p\n", this);
}

std::vector<counted_t<const datum_t> >
sub_t::get_els(batcher_t *batcher, const signal_t *interruptor) {
    assert_thread();
    guarantee(cond == NULL); // Can't get while blocking.
    auto_drainer_t::lock_t lock(&drainer);
    if (els.size() == 0 && !exc) {
        cond_t wait_for_data;
        cond = &wait_for_data;
        try {
            // We don't need to wait on the drain signal because the interruptor
            // will be pulsed if we're shutting down.
            wait_interruptible(cond, interruptor);
        } catch (const interrupted_exc_t &e) {
            cond = NULL;
            throw e;
        }
        guarantee(cond == NULL);
    }
    if (exc) {
        std::rethrow_exception(exc);
    }
    std::vector<counted_t<const datum_t> > v;
    while (els.size() > 0 && !batcher->should_send_batch()) {
        batcher->note_el(els.front());
        v.push_back(std::move(els.front()));
        els.pop_front();
    }
    guarantee(v.size() != 0);
    return std::move(v);
}

void sub_t::add_el(counted_t<const datum_t> d) {
    assert_thread();
    if (!exc) {
        els.push_back(d);
        if (els.size() > array_size_limit()) {
            els.clear();
            abort("Changefeed buffer over array size limit.  If you're making a lot of "
                  "changes to your data, make sure your client can keep up.");
        } else {
            maybe_signal_cond();
        }
    }
}

void sub_t::abort(const std::string &msg) {
    assert_thread();
    exc = std::make_exception_ptr(datum_exc_t(base_exc_t::GENERIC, msg));
    maybe_signal_cond();
}

void sub_t::maybe_signal_cond() THROWS_NOTHING {
    assert_thread();
    if (cond != NULL) {
        ASSERT_NO_CORO_WAITING;
        cond->pulse();
        cond = NULL;
    }
}

// If this throws we might leak the increment to `num_subs`.
void feed_t::add_sub(sub_t *sub) THROWS_NOTHING {
    on_thread_t th(home_thread());
    auto_drainer_t::lock_t lock(&drainer);
    num_subs += 1;
    rwlock_in_line_t spot(&subs_lock, access_t::write);
    spot.write_signal()->wait_lazily_unordered();
    subs[sub->home_thread().threadnum].insert(sub);
    if (detached) {
        sub->abort("Disconnected from peer.");
    }
}
// Can't throw because it's called in a destructor.
void feed_t::del_sub(sub_t *sub) THROWS_NOTHING {
    on_thread_t th(home_thread());
    {
        auto_drainer_t::lock_t lock(&drainer);
        rwlock_in_line_t spot(&subs_lock, access_t::write);
        spot.write_signal()->wait_lazily_unordered();
        size_t erased = subs[sub->home_thread().threadnum].erase(sub);
        guarantee(erased == 1);
    }
    num_subs -= 1;
    if (num_subs == 0) {
        // It's possible that by the time we get the lock to remove the feed,
        // another subscriber might have already found the feed and subscribed.
        client->maybe_remove_feed(uuid);
    }
}

// RSI: does this actually never throw?
template<class T>
void feed_t::each_sub(const T &t) THROWS_NOTHING {
    assert_thread();
    rwlock_in_line_t spot(&subs_lock, access_t::read);
    spot.read_signal()->wait_lazily_unordered();

    std::vector<int> sub_threads;
    for (int i = 0; i < get_num_threads(); ++i) {
        if (subs[i].size() != 0) {
            sub_threads.push_back(i);
        }
    }
    pmap(sub_threads.size(),
         [&t, &sub_threads, this](int i) {
             auto set = &subs[sub_threads[i]];
             guarantee(set->size() != 0);
             on_thread_t th((threadnum_t(sub_threads[i])));
             for (auto it = set->begin(); it != set->end(); ++it) {
                 debugf("%p: %d\n", (*it), (*it)->home_thread().threadnum);
                 t(*it);
             }
         });
}

bool feed_t::can_be_removed() {
    return num_subs == 0;
}

feed_t::feed_t(client_t *_client, base_namespace_repo_t *ns_repo, uuid_u _uuid)
    : client(_client),
      uuid(_uuid),
      manager(client->get_manager()),
      mailbox(manager, [this](changefeed::msg_t msg) { handle_msg(this, msg); }),
      subs(get_num_threads()),
      num_subs(0),
      detached(false) {
    // RSI: this doesn't do anything, does it?
    auto_drainer_t::lock_t lock(&drainer);
    base_namespace_repo_t::access_t access(ns_repo, uuid, lock.get_drain_signal());
    auto nif = access.get_namespace_if();
    write_t write(changefeed_subscribe_t(mailbox.get_address()),
                  profile_bool_t::DONT_PROFILE);
    write_response_t write_resp;
    // RSI: handle exceptions
    nif->write(write, &write_resp, order_token_t::ignore, lock.get_drain_signal());
    auto resp = boost::get<changefeed_subscribe_response_t>(&write_resp.response);
    guarantee(resp);
    stop_addrs = std::move(resp->addrs);
    std::set<peer_id_t> peers;
    for (auto it = stop_addrs.begin(); it != stop_addrs.end(); ++it) {
        peers.insert(it->get_peer());
    }
    for (auto it = peers.begin(); it != peers.end(); ++it) {
        disconnect_watchers.push_back(
            make_scoped<disconnect_watcher_t>(
                manager->get_connectivity_service(), *it));
        any_disconnect.add(&*disconnect_watchers.back());
    }
    // We spawn now to make sure `this` is valid when it runs.
    coro_t::spawn_now_dangerously(
        [this]() {
            auto_drainer_t::lock_t coro_lock(&drainer);
            wait_any_t wait_any(&any_disconnect, coro_lock.get_drain_signal());
            wait_any.wait_lazily_unordered();
            if (!detached) {
                detached = true;
                client->detach_feed(uuid);
                each_sub([](sub_t *sub) { sub->abort("Disconnected from peer."); });
            }
        }
    );
}

feed_t::~feed_t() {
    debugf("~feed_t()\n");
    guarantee(num_subs == 0);
    detached = true;
    // RSI: Are these sends expensive enough that we should avoid holding up
    // destruction for them?  Also, can they throw?
    for (auto it = stop_addrs.begin(); it != stop_addrs.end(); ++it) {
        send(manager, *it, mailbox.get_address());
    }
    debugf("~feed_t() DONE\n");
}

client_t::client_t(mailbox_manager_t *_manager) : manager(_manager) { }
client_t::~client_t() { }

counted_t<datum_stream_t>
client_t::new_feed(const counted_t<table_t> &tbl, env_t *env) {
    debugf("CLIENT: calling `new_feed`...\n");
    try {
        uuid_u uuid = tbl->get_uuid();
        scoped_ptr_t<sub_t> sub;
        {
            threadnum_t old_thread = get_thread_id();
            on_thread_t th(home_thread());
            debugf("CLIENT: On home thread...\n");
            auto_drainer_t::lock_t lock(&drainer);
            rwlock_in_line_t spot(&feeds_lock, access_t::write);
            spot.read_signal()->wait_lazily_unordered();
            debugf("CLIENT: getting feed...\n");
            auto feed_it = feeds.find(uuid);
            if (feed_it == feeds.end()) {
                debugf("CLIENT: making feed...\n");
                spot.write_signal()->wait_lazily_unordered();
                auto val = make_scoped<feed_t>(this, env->cluster_access.ns_repo, uuid);
                feed_it = feeds.insert(std::make_pair(uuid, std::move(val))).first;
            }
            on_thread_t th2(old_thread);
            sub.init(new sub_t(feed_it->second.get()));
        }
        return counted_t<datum_stream_t>(
            new stream_t(std::move(sub), tbl->backtrace()));
    } catch (const cannot_perform_query_exc_t &e) {
        rfail_datum(ql::base_exc_t::GENERIC,
                    "cannot subscribe to table `%s`: %s",
                    tbl->name.c_str(), e.what());
    }
}

// This is called when there are no more subs for a feed.  It might not do
// anything if another sub managed to find the feed before it gets the lock.
void client_t::maybe_remove_feed(const uuid_u &uuid) {
    debugf("CLIENT: maybe_remove_feed...\n");
    assert_thread();
    auto_drainer_t::lock_t lock(&drainer);
    rwlock_in_line_t spot(&feeds_lock, access_t::write);
    spot.write_signal()->wait_lazily_unordered();
    auto feed_it = feeds.find(uuid);
    if (feed_it == feeds.end()) {
        auto detached_feed_it = detached_feeds.find(uuid);
        guarantee(detached_feed_it != detached_feeds.end());
        if (detached_feed_it->second->can_be_removed()) {
            debugf("CLIENT: removing DETACHED_feed...\n");
            detached_feeds.erase(detached_feed_it);
        }
    } else {
        if (feed_it->second->can_be_removed()) {
            debugf("CLIENT: removing feed...\n");
            feeds.erase(feed_it);
        }
    }
    debugf("CLIENT: maybe remove feed DONE!\n");
}

// This detaches a feed so that no new subs can find it.  This is used when one
// of our peers that we're reading changes from disconnects.  We need to keep
// the feed somewhere until all the subs are done with it, but we don't want to
// let new subs find it because the user might bring the disconnected peer back
// up and want to start a new feed before the old feed is destroyed.
void client_t::detach_feed(const uuid_u &uuid) {
    assert_thread();
    auto_drainer_t::lock_t lock(&drainer);
    rwlock_in_line_t spot(&feeds_lock, access_t::write);
    spot.write_signal()->wait_lazily_unordered();
    auto feed_it = feeds.find(uuid);
    guarantee(feed_it != feeds.end());
    detached_feeds.insert(std::make_pair(uuid, std::move(feed_it->second)));
    feeds.erase(feed_it);
}

} // namespace changefeed
} // namespace ql
