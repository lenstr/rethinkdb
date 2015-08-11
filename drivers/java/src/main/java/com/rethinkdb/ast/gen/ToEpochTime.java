// Autogenerated by metajava.py.
// Do not edit this file directly.
// The template for this file is located at:
// ../../../../../../../../templates/AstSubclass.java
package com.rethinkdb.ast.gen;

import com.rethinkdb.model.Arguments;
import com.rethinkdb.model.OptArgs;
import com.rethinkdb.ast.ReqlAst;
import com.rethinkdb.proto.TermType;


public class ToEpochTime extends ReqlQuery {


    public ToEpochTime(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public ToEpochTime(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public ToEpochTime(ReqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.TO_EPOCH_TIME, args, optargs);
    }
    protected ToEpochTime(ReqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


    /* Static factories */
    public static ToEpochTime fromArgs(java.lang.Object... args){
        return new ToEpochTime(new Arguments(args), null);
    }


}
