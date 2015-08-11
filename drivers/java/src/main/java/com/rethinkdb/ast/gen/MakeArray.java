// Autogenerated by metajava.py.
// Do not edit this file directly.
// The template for this file is located at:
// ../../../../../../../../templates/AstSubclass.java
package com.rethinkdb.ast.gen;

import com.rethinkdb.model.Arguments;
import com.rethinkdb.model.OptArgs;
import com.rethinkdb.ast.ReqlAst;
import com.rethinkdb.proto.TermType;


public class MakeArray extends ReqlQuery {


    public MakeArray(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public MakeArray(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public MakeArray(ReqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.MAKE_ARRAY, args, optargs);
    }
    protected MakeArray(ReqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


    /* Static factories */
    public static MakeArray fromArgs(java.lang.Object... args){
        return new MakeArray(new Arguments(args), null);
    }


}
