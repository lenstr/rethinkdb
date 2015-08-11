// Autogenerated by metajava.py.
// Do not edit this file directly.
// The template for this file is located at:
// ../../../../../../../../templates/AstSubclass.java
package com.rethinkdb.ast.gen;

import com.rethinkdb.model.Arguments;
import com.rethinkdb.model.OptArgs;
import com.rethinkdb.ast.ReqlAst;
import com.rethinkdb.proto.TermType;


public class Maxval extends ReqlQuery {


    public Maxval(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public Maxval(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public Maxval(ReqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.MAXVAL, args, optargs);
    }
    protected Maxval(ReqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


    /* Static factories */
    public static Maxval fromArgs(java.lang.Object... args){
        return new Maxval(new Arguments(args), null);
    }


}
