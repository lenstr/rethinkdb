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



public class IsEmpty extends ReqlQuery {


    public IsEmpty(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public IsEmpty(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public IsEmpty(ReqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.IS_EMPTY, args, optargs);
    }
    protected IsEmpty(ReqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


    /* Static factories */
    public static IsEmpty fromArgs(Object... args){
        return new IsEmpty(new Arguments(args), null);
    }


}
