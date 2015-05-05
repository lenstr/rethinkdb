// Autogenerated by convert_protofile.py on 2015-05-04.
// Do not edit this file directly.
// The template for this file is located at:
// ../../../../../../../../templates/AstSubclass.java
package com.rethinkdb.ast.gen;

import com.rethinkdb.ast.helper.Arguments;
import com.rethinkdb.ast.helper.OptArgs;
import com.rethinkdb.ast.RqlAst;
import com.rethinkdb.proto.TermType;
import java.util.*;

public class RqlQuery extends RqlAst {

    protected RqlQuery(RqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }
    /* Query level terms */
    public Eq eq(Object... fields) {
        return new Eq(this, new Arguments(fields), new OptArgs());
    }

    public Ne ne(Object... fields) {
        return new Ne(this, new Arguments(fields), new OptArgs());
    }

    public Lt lt(Object... fields) {
        return new Lt(this, new Arguments(fields), new OptArgs());
    }

    public Le le(Object... fields) {
        return new Le(this, new Arguments(fields), new OptArgs());
    }

    public Gt gt(Object... fields) {
        return new Gt(this, new Arguments(fields), new OptArgs());
    }

    public Ge ge(Object... fields) {
        return new Ge(this, new Arguments(fields), new OptArgs());
    }

    public Not not(Object... fields) {
        return new Not(this, new Arguments(fields), new OptArgs());
    }

    public Add add(Object... fields) {
        return new Add(this, new Arguments(fields), new OptArgs());
    }

    public Sub sub(Object... fields) {
        return new Sub(this, new Arguments(fields), new OptArgs());
    }

    public Mul mul(Object... fields) {
        return new Mul(this, new Arguments(fields), new OptArgs());
    }

    public Div div(Object... fields) {
        return new Div(this, new Arguments(fields), new OptArgs());
    }

    public Mod mod(Object... fields) {
        return new Mod(this, new Arguments(fields), new OptArgs());
    }

    public Floor floor(Object... fields) {
        return new Floor(this, new Arguments(fields), new OptArgs());
    }

    public Ceil ceil(Object... fields) {
        return new Ceil(this, new Arguments(fields), new OptArgs());
    }

    public Round round(Object... fields) {
        return new Round(this, new Arguments(fields), new OptArgs());
    }

    public Append append(Object... fields) {
        return new Append(this, new Arguments(fields), new OptArgs());
    }

    public Prepend prepend(Object... fields) {
        return new Prepend(this, new Arguments(fields), new OptArgs());
    }

    public Difference difference(Object... fields) {
        return new Difference(this, new Arguments(fields), new OptArgs());
    }

    public SetInsert setInsert(Object... fields) {
        return new SetInsert(this, new Arguments(fields), new OptArgs());
    }

    public SetIntersection setIntersection(Object... fields) {
        return new SetIntersection(this, new Arguments(fields), new OptArgs());
    }

    public SetUnion setUnion(Object... fields) {
        return new SetUnion(this, new Arguments(fields), new OptArgs());
    }

    public SetDifference setDifference(Object... fields) {
        return new SetDifference(this, new Arguments(fields), new OptArgs());
    }

    public Slice slice(Object... fields) {
        return new Slice(this, new Arguments(fields), new OptArgs());
    }

    public Skip skip(Object... fields) {
        return new Skip(this, new Arguments(fields), new OptArgs());
    }

    public Limit limit(Object... fields) {
        return new Limit(this, new Arguments(fields), new OptArgs());
    }

    public OffsetsOf offsetsOf(Object... fields) {
        return new OffsetsOf(this, new Arguments(fields), new OptArgs());
    }

    public Contains contains(Object... fields) {
        return new Contains(this, new Arguments(fields), new OptArgs());
    }

    public GetField getField(Object... fields) {
        return new GetField(this, new Arguments(fields), new OptArgs());
    }

    public Keys keys(Object... fields) {
        return new Keys(this, new Arguments(fields), new OptArgs());
    }

    public HasFields hasFields(Object... fields) {
        return new HasFields(this, new Arguments(fields), new OptArgs());
    }

    public WithFields withFields(Object... fields) {
        return new WithFields(this, new Arguments(fields), new OptArgs());
    }

    public Pluck pluck(Object... fields) {
        return new Pluck(this, new Arguments(fields), new OptArgs());
    }

    public Without without(Object... fields) {
        return new Without(this, new Arguments(fields), new OptArgs());
    }

    public Merge merge(Object... fields) {
        return new Merge(this, new Arguments(fields), new OptArgs());
    }

    public Between between(Object... fields) {
        return new Between(this, new Arguments(fields), new OptArgs());
    }

    public Reduce reduce(Object... fields) {
        return new Reduce(this, new Arguments(fields), new OptArgs());
    }

    public Map map(Object... fields) {
        return new Map(this, new Arguments(fields), new OptArgs());
    }

    public Filter filter(Object... fields) {
        return new Filter(this, new Arguments(fields), new OptArgs());
    }

    public ConcatMap concatMap(Object... fields) {
        return new ConcatMap(this, new Arguments(fields), new OptArgs());
    }

    public OrderBy orderBy(Object... fields) {
        return new OrderBy(this, new Arguments(fields), new OptArgs());
    }

    public Distinct distinct(Object... fields) {
        return new Distinct(this, new Arguments(fields), new OptArgs());
    }

    public Count count(Object... fields) {
        return new Count(this, new Arguments(fields), new OptArgs());
    }

    public IsEmpty isEmpty(Object... fields) {
        return new IsEmpty(this, new Arguments(fields), new OptArgs());
    }

    public Union union(Object... fields) {
        return new Union(this, new Arguments(fields), new OptArgs());
    }

    public Nth nth(Object... fields) {
        return new Nth(this, new Arguments(fields), new OptArgs());
    }

    public Bracket field(Object... fields) {
        return new Bracket(this, new Arguments(fields), new OptArgs());
    }

    public InnerJoin innerJoin(Object... fields) {
        return new InnerJoin(this, new Arguments(fields), new OptArgs());
    }

    public OuterJoin outerJoin(Object... fields) {
        return new OuterJoin(this, new Arguments(fields), new OptArgs());
    }

    public EqJoin eqJoin(Object... fields) {
        return new EqJoin(this, new Arguments(fields), new OptArgs());
    }

    public Zip zip(Object... fields) {
        return new Zip(this, new Arguments(fields), new OptArgs());
    }

    public InsertAt insertAt(Object... fields) {
        return new InsertAt(this, new Arguments(fields), new OptArgs());
    }

    public DeleteAt deleteAt(Object... fields) {
        return new DeleteAt(this, new Arguments(fields), new OptArgs());
    }

    public ChangeAt changeAt(Object... fields) {
        return new ChangeAt(this, new Arguments(fields), new OptArgs());
    }

    public SpliceAt spliceAt(Object... fields) {
        return new SpliceAt(this, new Arguments(fields), new OptArgs());
    }

    public CoerceTo coerceTo(Object... fields) {
        return new CoerceTo(this, new Arguments(fields), new OptArgs());
    }

    public TypeOf typeOf(Object... fields) {
        return new TypeOf(this, new Arguments(fields), new OptArgs());
    }

    public Update update(Object... fields) {
        return new Update(this, new Arguments(fields), new OptArgs());
    }

    public Delete delete(Object... fields) {
        return new Delete(this, new Arguments(fields), new OptArgs());
    }

    public Replace replace(Object... fields) {
        return new Replace(this, new Arguments(fields), new OptArgs());
    }

    public Funcall do_(Object... fields) {
        return new Funcall(this, new Arguments(fields), new OptArgs());
    }

    public Or or(Object... fields) {
        return new Or(this, new Arguments(fields), new OptArgs());
    }

    public And and(Object... fields) {
        return new And(this, new Arguments(fields), new OptArgs());
    }

    public ForEach forEach(Object... fields) {
        return new ForEach(this, new Arguments(fields), new OptArgs());
    }

    public Info info(Object... fields) {
        return new Info(this, new Arguments(fields), new OptArgs());
    }

    public Match match(Object... fields) {
        return new Match(this, new Arguments(fields), new OptArgs());
    }

    public Upcase upcase(Object... fields) {
        return new Upcase(this, new Arguments(fields), new OptArgs());
    }

    public Downcase downcase(Object... fields) {
        return new Downcase(this, new Arguments(fields), new OptArgs());
    }

    public Sample sample(Object... fields) {
        return new Sample(this, new Arguments(fields), new OptArgs());
    }

    public Default default_(Object... fields) {
        return new Default(this, new Arguments(fields), new OptArgs());
    }

    public ToJsonString to_json(Object... fields) {
        return new ToJsonString(this, new Arguments(fields), new OptArgs());
    }

    public ToIso8601 toIso8601(Object... fields) {
        return new ToIso8601(this, new Arguments(fields), new OptArgs());
    }

    public ToEpochTime toEpochTime(Object... fields) {
        return new ToEpochTime(this, new Arguments(fields), new OptArgs());
    }

    public InTimezone inTimezone(Object... fields) {
        return new InTimezone(this, new Arguments(fields), new OptArgs());
    }

    public During during(Object... fields) {
        return new During(this, new Arguments(fields), new OptArgs());
    }

    public Date date(Object... fields) {
        return new Date(this, new Arguments(fields), new OptArgs());
    }

    public TimeOfDay timeOfDay(Object... fields) {
        return new TimeOfDay(this, new Arguments(fields), new OptArgs());
    }

    public Timezone timezone(Object... fields) {
        return new Timezone(this, new Arguments(fields), new OptArgs());
    }

    public Year year(Object... fields) {
        return new Year(this, new Arguments(fields), new OptArgs());
    }

    public Month month(Object... fields) {
        return new Month(this, new Arguments(fields), new OptArgs());
    }

    public Day day(Object... fields) {
        return new Day(this, new Arguments(fields), new OptArgs());
    }

    public DayOfWeek dayOfWeek(Object... fields) {
        return new DayOfWeek(this, new Arguments(fields), new OptArgs());
    }

    public DayOfYear dayOfYear(Object... fields) {
        return new DayOfYear(this, new Arguments(fields), new OptArgs());
    }

    public Hours hours(Object... fields) {
        return new Hours(this, new Arguments(fields), new OptArgs());
    }

    public Minutes minutes(Object... fields) {
        return new Minutes(this, new Arguments(fields), new OptArgs());
    }

    public Seconds seconds(Object... fields) {
        return new Seconds(this, new Arguments(fields), new OptArgs());
    }

    public April april(Object... fields) {
        return new April(this, new Arguments(fields), new OptArgs());
    }

    public Group group(Object... fields) {
        return new Group(this, new Arguments(fields), new OptArgs());
    }

    public Sum sum(Object... fields) {
        return new Sum(this, new Arguments(fields), new OptArgs());
    }

    public Avg avg(Object... fields) {
        return new Avg(this, new Arguments(fields), new OptArgs());
    }

    public Min min(Object... fields) {
        return new Min(this, new Arguments(fields), new OptArgs());
    }

    public Max max(Object... fields) {
        return new Max(this, new Arguments(fields), new OptArgs());
    }

    public Split split(Object... fields) {
        return new Split(this, new Arguments(fields), new OptArgs());
    }

    public Ungroup ungroup(Object... fields) {
        return new Ungroup(this, new Arguments(fields), new OptArgs());
    }

    public Changes changes(Object... fields) {
        return new Changes(this, new Arguments(fields), new OptArgs());
    }

    public ToGeojson toGeojson(Object... fields) {
        return new ToGeojson(this, new Arguments(fields), new OptArgs());
    }

    public Distance distance(Object... fields) {
        return new Distance(this, new Arguments(fields), new OptArgs());
    }

    public Intersects intersects(Object... fields) {
        return new Intersects(this, new Arguments(fields), new OptArgs());
    }

    public Includes includes(Object... fields) {
        return new Includes(this, new Arguments(fields), new OptArgs());
    }

    public Fill fill(Object... fields) {
        return new Fill(this, new Arguments(fields), new OptArgs());
    }

    public PolygonSub polygonSub(Object... fields) {
        return new PolygonSub(this, new Arguments(fields), new OptArgs());
    }

}
