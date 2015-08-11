// Autogenerated by metajava.py.
// Do not edit this file directly.
// The template for this file is located at:
// ../../../../../../../../templates/AstSubclass.java
package com.rethinkdb.ast.gen;

import com.rethinkdb.model.Arguments;
import com.rethinkdb.model.OptArgs;
import com.rethinkdb.ast.ReqlAst;
import com.rethinkdb.proto.TermType;


public class Div extends ReqlQuery {


    public Div(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public Div(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public Div(ReqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.DIV, args, optargs);
    }
    protected Div(ReqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


    /* Static factories */
    public static Div fromArgs(java.lang.Object... args){
        return new Div(new Arguments(args), null);
    }


}
