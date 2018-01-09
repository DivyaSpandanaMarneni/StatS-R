package com.extension;

import com.rIntegration.Median;
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
 * Created by Divya on 8/13/2017.
 */
public class CustomAggregateMedian {

    public static ArrayList<Double> arrayList = new ArrayList<>();

    static AccumulatorFactory medianAccumulatorFactory = new AccumulatorFactory() {
        @Override
        public Accumulator createAccumulator(AggCustom agg, boolean distinct) {
            return new MedianAccumulator(agg);
        }
    };

    static {
        String medianURI = "http://example.org/function#median";
        AggregateRegistry.register(medianURI,medianAccumulatorFactory);
    }

    public static void init(){
        String aggUri = "http://example.org/function#median" ;
        AggregateRegistry.register(aggUri,medianAccumulatorFactory, NodeConst.nodeMinusOne);
    }

    static class MedianAccumulator implements Accumulator{

        double medianValue = 0;
        private AggCustom aggCustom;

        MedianAccumulator(AggCustom aggCustom){
            this.aggCustom = aggCustom;
        }



        @Override
        public void accumulate(Binding binding, FunctionEnv functionEnv) {
            ExprList exprList = aggCustom.getExprList();
            for (Expr expr :
                    exprList) {
                try{
                    NodeValue nv = expr.eval(binding,functionEnv);
                    if(nv.isLiteral()){
                        arrayList.add(Double.parseDouble(nv.asUnquotedString()));
                    }

                }catch (ExprEvalException ee){
                    ee.printStackTrace();
                }
            }

        }

        @Override
        public NodeValue getValue() {
            Median median = new Median();
            medianValue = median.getMedian(arrayList);
            DecimalFormat decimalFormat = new DecimalFormat("#.#####");
            double result = Double.parseDouble(decimalFormat.format(medianValue));
            return NodeValue.makeDecimal(result);
        }
    }


}
