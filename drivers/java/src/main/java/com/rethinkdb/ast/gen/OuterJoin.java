// Autogenerated by convert_protofile.py on 2015-05-07.
// Do not edit this file directly.
// The template for this file is located at:
// ../../../../../../../../templates/AstSubclass.java
package com.rethinkdb.ast.gen;

import com.rethinkdb.ast.helper.Arguments;
import com.rethinkdb.ast.helper.OptArgs;
import com.rethinkdb.ast.RqlAst;
import com.rethinkdb.proto.TermType;
import java.util.*;



public class OuterJoin extends RqlQuery {


    public OuterJoin(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public OuterJoin(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public OuterJoin(RqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.OUTER_JOIN, args, optargs);
    }
    protected OuterJoin(RqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


   /* Static factories */
    public static OuterJoin fromArgs(Object... args){
        return new OuterJoin(new Arguments(args), null);
    }


}
