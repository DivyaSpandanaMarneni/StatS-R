package com.extension;

import com.rIntegration.Skewness;
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
 * Created by Divya on 9/2/2017.
 */
public class CustomAggregateSkewness {
    public static ArrayList<Double> arrayList = new ArrayList<>();


    static AccumulatorFactory myAccumulatorFacotry = new AccumulatorFactory() {

        @Override
        public Accumulator createAccumulator(AggCustom agg, boolean distinct) {
            return new CustomAggregateSkewness.SkewnessAccumulator(agg);
        }
    };

    static {
        String aggUri = "http://example.org/function#skewness" ;
        AggregateRegistry.register(aggUri,myAccumulatorFacotry, NodeConst.nodeMinusOne);
    }

    public static void init(){
        String aggUri = "http://example.org/function#skewness" ;
        AggregateRegistry.register(aggUri,myAccumulatorFacotry, NodeConst.nodeMinusOne);
    }


    static class SkewnessAccumulator implements Accumulator{
        double skewnessValue = 0;
        private AggCustom aggCustom;

        SkewnessAccumulator(AggCustom aggCustom){
            this.aggCustom = aggCustom;
        }

        @Override
        public void accumulate(Binding binding, FunctionEnv functionEnv) {
            ExprList exprList = aggCustom.getExprList();
            for (Expr expr :
                    exprList) {
                try{
                    NodeValue nv = expr.eval(binding, functionEnv);
                    if(nv.isLiteral()){
                        arrayList.add(Double.parseDouble(nv.asUnquotedString()));
                    }
                }catch (ExprEvalException ex) {
                    ex.printStackTrace();
                }
            }

        }
        @Override
        public NodeValue getValue() {
            Skewness skewness = new Skewness();
            skewnessValue = skewness.getSkewness(arrayList);
            DecimalFormat decimalFormat = new DecimalFormat("#.#####");
            double result = Double.parseDouble(decimalFormat.format(skewnessValue));
            return NodeValue.makeDecimal(result);
        }
    }

}
