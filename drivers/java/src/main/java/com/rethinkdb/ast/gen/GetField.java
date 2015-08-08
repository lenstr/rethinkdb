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



public class GetField extends ReqlQuery {


    public GetField(java.lang.Object arg) {
        this(new Arguments(arg), null);
    }
    public GetField(Arguments args, OptArgs optargs) {
        this(null, args, optargs);
    }
    public GetField(ReqlAst prev, Arguments args, OptArgs optargs) {
        this(prev, TermType.GET_FIELD, args, optargs);
    }
    protected GetField(ReqlAst previous, TermType termType, Arguments args, OptArgs optargs){
        super(previous, termType, args, optargs);
    }


    /* Static factories */
    public static GetField fromArgs(Object... args){
        return new GetField(new Arguments(args), null);
    }


}
