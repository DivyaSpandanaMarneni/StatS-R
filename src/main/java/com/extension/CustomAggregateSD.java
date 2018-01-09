package com.extension;



//import com.RLayer.StandardDeviation;
import com.rIntegration.StandardDeviation;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Divya on 4/20/2017.
 */
public class CustomAggregateSD {

    public static  ArrayList<Double> arrayList = new ArrayList<>();
    //public static Double stanDeviation;

    static AccumulatorFactory myAccumulatorFacotry = new AccumulatorFactory() {
        @Override
        public Accumulator createAccumulator(AggCustom agg, boolean distinct) {
            return new SDAccumulator(agg);
        }
    };

    static {
        String aggUri = "http://example.org/function#sd" ;
        AggregateRegistry.register(aggUri,myAccumulatorFacotry, NodeConst.nodeMinusOne);
    }

    public static void init(){
        String aggUri = "http://example.org/function#sd" ;
        AggregateRegistry.register(aggUri,myAccumulatorFacotry, NodeConst.nodeMinusOne);
    }

    static class SDAccumulator implements Accumulator{

        double sd = 0;
        private AggCustom aggCustom;

        SDAccumulator(AggCustom aggCustom){
            this.aggCustom = aggCustom;
        }


        @Override
        public void accumulate(Binding binding, FunctionEnv functionEnv) {
            ExprList exprList = aggCustom.getExprList();

            for (Expr expr :
                    exprList) {

                try {
                    NodeValue nv = expr.eval(binding, functionEnv);
                    // Evaluation succeeded.
                    if ( nv.isLiteral()) {
                        arrayList.add(Double.parseDouble(nv.asUnquotedString()));
                    }
                } catch (ExprEvalException ex) {
                }

            }


            //System.out.println("Standard deviation is "+sd);

        }


        public NodeValue getValue()
        {
            StandardDeviation standardDeviation = new StandardDeviation();
            sd = standardDeviation.getSD(arrayList);
            DecimalFormat decimalFormat = new DecimalFormat("#.#####");
            double result = Double.parseDouble(decimalFormat.format(sd));
            return NodeValue.makeDecimal(result);
        }
    }



}
