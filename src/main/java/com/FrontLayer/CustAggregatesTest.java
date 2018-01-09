package com.FrontLayer;

import org.apache.jena.base.Sys;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.aggregate.Accumulator;
import org.apache.jena.sparql.expr.aggregate.AccumulatorFactory;
import org.apache.jena.sparql.expr.aggregate.AggCustom;
import org.apache.jena.sparql.expr.aggregate.AggregateRegistry;
import org.apache.jena.sparql.function.FunctionEnv;
import org.apache.jena.sparql.graph.NodeConst;
import org.apache.jena.sparql.sse.SSE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Divya on 4/19/2017.
 */
public class CustAggregatesTest {

    static AccumulatorFactory myAccumulatorFactory = new AccumulatorFactory() {
        @Override
        public Accumulator createAccumulator(AggCustom agg, boolean distinct) {
            return new MyAccumulator(agg);
        }
    };


    static class MyAccumulator implements Accumulator {
        int count = 0;
        private AggCustom agg;

        MyAccumulator(AggCustom agg) {
            this.agg = agg;
        }

        public void accumulate(Binding binding, FunctionEnv functionEnv) {
            ExprList exprList = agg.getExprList();

            for (Expr expr :
                    exprList) {

                try {
                    NodeValue nv = expr.eval(binding, functionEnv);
                    // Evaluation succeeded.
                    if (nv.isLiteral())
                        count++;
                } catch (ExprEvalException ex) {
                }

            }



        }

        public NodeValue getValue() {
            System.out.println("The count in getValue "+count);
            return NodeValue.makeInteger(count);
        }
    }

        public static void main(String[] args) throws FileNotFoundException{

            // Example aggregate that counts literals.
            // Returns unbound for no rows.
            String aggUri = "http://example/countLiterals" ;

            /* Registration */
            AggregateRegistry.register(aggUri, myAccumulatorFactory, NodeConst.nodeMinusOne);


            // Some data.
            //Graph g = SSE.parseGraph("(graph (:s :p :o) (:s :p 1))") ;
            String qs = "SELECT (<http://example/countLiterals>(?o) AS ?x) where {?s ?p ?o .}" ;

            // Execution as normal.

            Model model = ModelFactory.createDefaultModel();
            String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\Traffic.rdf";
            FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
            model.read(fileInputStream,null);

            Dataset dataset = DatasetFactory.create(model);

            Query q = QueryFactory.create(qs) ;
            try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
                ResultSet rs = qexec.execSelect() ;
                ResultSetFormatter.out(rs);
            }catch (Exception e){
                e.printStackTrace();
            }
    }
}
