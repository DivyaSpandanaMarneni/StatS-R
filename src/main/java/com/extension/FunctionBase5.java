package com.extension;

import org.apache.jena.atlas.lib.Lib;
import org.apache.jena.query.QueryBuildException;
import org.apache.jena.sparql.ARQInternalErrorException;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase;
import org.apache.jena.sparql.function.FunctionEnv;

import java.util.List;

/**
 * Created by Divya on 12/16/2017.
 */
public abstract class FunctionBase5  extends FunctionBase{

    @Override
    public void checkBuild(String uri, ExprList args)
    {
        if ( args.size() != 5 )
            throw new QueryBuildException("Function '"+ Lib.className(this)+"' takes five arguments") ;
    }

    @Override
    public NodeValue exec(List<NodeValue> args) {
        if ( args == null )
            // The contract on the function interface is that this should not happen.
            throw new ARQInternalErrorException(Lib.className(this)+": Null args list") ;

        if ( args.size() != 5 )
            throw new ExprEvalException(Lib.className(this)+": Wrong number of arguments: Wanted 4, got "+args.size()) ;

        NodeValue v1 = args.get(0) ;
        NodeValue v2 = args.get(1) ;
        NodeValue v3 = args.get(2) ;
        NodeValue v4 = args.get(3) ;
        NodeValue v5 = args.get(4) ;

        return exec(v1, v2, v3, v4, v5) ;
    }

    public abstract NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3, NodeValue v4, NodeValue v5) ;
}
