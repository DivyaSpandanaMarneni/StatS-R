package com.extension;

import com.rIntegration.Mean;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomAggregateMean {

    private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("CustomAggregateMean");

    public static ArrayList<Double> arrayList = new ArrayList<>();


    static AccumulatorFactory myAccumulatorFactory = new AccumulatorFactory() {

        @Override
        public Accumulator createAccumulator(AggCustom agg, boolean distinct) {
            return new MeanAccumulator(agg);
        }
    };

    static {
        //String aggUri = "http://example/getMean" ;
        String aggUri = "http://example.org/function#mean" ;
        AggregateRegistry.register(aggUri,myAccumulatorFactory);
    }

    public CustomAggregateMean() {
        LOGGER.info("*********************** ENTERED CUSTOMAGGREGATEMEAN ****************************");
    }

    public static void init(){
        LOGGER.info("****************** INITIATED CUSTOMAGGREGATEMEAN **********************");
        String aggUri = "http://example.org/function#mean" ;
        AggregateRegistry.register(aggUri,myAccumulatorFactory);
    }


    static class MeanAccumulator implements Accumulator{
        double meanValue = 0;
        private AggCustom aggCustom;

        MeanAccumulator(AggCustom aggCustom){
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
            Mean mean = new Mean();
            meanValue = mean.getMean(arrayList);
            DecimalFormat decimalFormat = new DecimalFormat("#.#####");
            double result = Double.parseDouble(decimalFormat.format(meanValue));
            return NodeValue.makeDecimal(result);
        }
    }





}
