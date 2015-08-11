// Autogenerated by metajava.py.
// Do not edit this file directly.
// The template for this file is located at:
// ../../../../../../../../templates/AstSubclass.java
package com.rethinkdb.ast.gen;

import com.rethinkdb.model.Arguments;
import com.rethinkdb.model.OptArgs;
import com.rethinkdb.ast.ReqlAst;
import com.rethinkdb.proto.TermType;


public class Line extends ReqlQuery {


    public Line(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public Line(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public Line(ReqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.LINE, args, optargs);
    }
    protected Line(ReqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


    /* Static factories */
    public static Line fromArgs(java.lang.Object... args){
        return new Line(new Arguments(args), null);
    }


}
