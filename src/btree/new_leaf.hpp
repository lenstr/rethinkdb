// Copyright 2010-2014 RethinkDB, all rights reserved.
#ifndef BTREE_NEW_LEAF_HPP_
#define BTREE_NEW_LEAF_HPP_

#include "containers/sized_ptr.hpp"
#include "errors.hpp"
#include "serializer/types.hpp"

struct btree_key_t;
class buf_write_t;
class buf_ptr_t;
struct main_leaf_node_t;

namespace new_leaf {

template <class btree_type>
class new_leaf_t {
public:
    static buf_ptr_t init();

    static MUST_USE bool find_key(sized_ptr_t<const main_leaf_node_t> node,
                                  const btree_key_t *key,
                                  int *index_out);

    static void insert_entry(default_block_size_t bs,
                             buf_write_t *buf,
                             const void *entry);

    static void erase_presence(default_block_size_t bs,
                               buf_write_t *buf,
                               const btree_key_t *key);

    static MUST_USE bool lookup_entry(sized_ptr_t<const main_leaf_node_t> node,
                                      const btree_key_t *key,
                                      const void **entry_ptr_out);

    static bool is_empty(sized_ptr_t<const main_leaf_node_t> node);

    static bool is_full(default_block_size_t bs, const main_leaf_node_t *node,
                        const void *entry);

#ifndef NDEBUG
    static void validate(default_block_size_t bs, sized_ptr_t<const main_leaf_node_t> node);
#else
    static void validate(default_block_size_t, sized_ptr_t<const main_leaf_node_t>) { }
#endif
};

}  // namespace new_leaf

using new_leaf::new_leaf_t;

#endif /* BTREE_NEW_LEAF_HPP_ */