// Autogenerated by metajava.py.
// Do not edit this file directly.
// The template for this file is located at:
// ../../../../../../../../templates/AstSubclass.java
package com.rethinkdb.ast.gen;

import java.util.Optional;
import com.rethinkdb.ast.ReqlAst;
import com.rethinkdb.ast.helper.Arguments;
import com.rethinkdb.ast.helper.OptArgs;
import com.rethinkdb.proto.TermType;



public class Datum extends ReqlQuery {


    public Datum(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public Datum(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public Datum(ReqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.DATUM, args, optargs);
    }
    protected Datum(ReqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


    /* Static factories */
    public static Datum fromArgs(Object... args){
        return new Datum(new Arguments(args), null);
    }


}
