package com.extension;

import org.apache.jena.graph.Node;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.aggregate.*;
import org.apache.jena.sparql.expr.nodevalue.XSDFuncOp;
import org.apache.jena.sparql.function.FunctionEnv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Divya on 4/23/2017.
 */
public class AggAvgCheck extends AggregatorBase {

    // ---- AVG(?var)
    public AggAvgCheck(Expr expr) { super("AVG", false, expr) ; }
    @Override
    public Aggregator copy(ExprList expr) { return new AggAvgCheck(expr.get(0)) ; }

    // XQuery/XPath Functions&Operators suggests zero
    // SQL suggests null.
    private static final NodeValue noValuesToAvg = NodeValue.nvZERO ; // null

    @Override
    public Accumulator createAccumulator()
    {
        return new AccAvg(getExpr()) ;
    }

    @Override
    public Node getValueEmpty()     { return NodeValue.toNode(noValuesToAvg) ; }

    @Override
    public int hashCode()   { return HC_AggAvg ^ getExprList().hashCode() ; }

    @Override
    public boolean equals(Aggregator other, boolean bySyntax) {
        if ( other == null ) return false ;
        if ( this == other ) return true ;
        if ( ! ( other instanceof AggAvg ) ) return false ;
        AggAvgCheck a = (AggAvgCheck)other ;
        return exprList.equals(a.exprList, bySyntax) ;
    }

    // ---- Accumulator
    private static class AccAvg extends AccumulatorExpr
    {
        // Non-empty case but still can be nothing because the expression may be undefined.
        private NodeValue total = noValuesToAvg ;
        private int count = 0 ;

        static final boolean DEBUG = false ;

        public AccAvg(Expr expr) { super(expr, false) ; }

        @Override
        protected void accumulate(NodeValue nv, Binding binding, FunctionEnv functionEnv)
        {
            if ( DEBUG ) System.out.println("avg: "+nv) ;

            if ( nv.isNumber() )
            {
                count++ ;
                if ( total == noValuesToAvg )
                    total = nv ;
                else
                    total = XSDFuncOp.numAdd(nv, total) ;
            }
            else
            {
                //ARQ.getExecLogger().warn("Evaluation error: avg() on "+nv) ;
                throw new ExprEvalException("avg: not a number: "+nv) ;
            }

            if ( DEBUG ) System.out.println("avg: ("+total+","+count+")") ;
        }

        @Override
        protected void accumulateError(Binding binding, FunctionEnv functionEnv)
        {}

        @Override
        public NodeValue getAccValue()
        {
            if ( count == 0 ) return noValuesToAvg ;
            if ( super.errorCount != 0 )
                //throw new ExprEvalException("avg: error in group") ;
                return null ;
            NodeValue nvCount = NodeValue.makeInteger(count) ;
            return XSDFuncOp.numDivide(total, nvCount) ;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        CustomAggregateSD customAggregateSD = new CustomAggregateSD();

        String aggUri = "http://example/getSD" ;
        //AggregateRegistry.register(aggUri,aggregatorFactory,noValuesForSD);
        //AggregateRegistry.register(aggUri,, NodeConst.nodeMinusOne);

        //String qs = "SELECT (<http://example/getSD>(?o) AS ?sd) {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;
        String qs = "SELECT (MAX(?o) AS ?max) where {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;

        String qsTest = "SELECT ?s ?p ?o where {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o } ";


        Model model = ModelFactory.createDefaultModel();
        String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\ChronicDiseases.rdf";
        FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
        model.read(fileInputStream,null);

        Dataset dataset = DatasetFactory.create(model);

        Query q = QueryFactory.create(qs) ;
        try ( QueryExecution qexec = QueryExecutionFactory.create(q, model) ) {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(rs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
