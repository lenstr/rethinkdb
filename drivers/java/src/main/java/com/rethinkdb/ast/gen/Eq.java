// Autogenerated by metajava.py.
// Do not edit this file directly.
// The template for this file is located at:
// ../../../../../../../../templates/AstSubclass.java
package com.rethinkdb.ast.gen;

import com.rethinkdb.model.Arguments;
import com.rethinkdb.model.OptArgs;
import com.rethinkdb.ast.ReqlAst;
import com.rethinkdb.proto.TermType;


public class Eq extends ReqlQuery {


    public Eq(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public Eq(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public Eq(ReqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.EQ, args, optargs);
    }
    protected Eq(ReqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


    /* Static factories */
    public static Eq fromArgs(java.lang.Object... args){
        return new Eq(new Arguments(args), null);
    }


}
