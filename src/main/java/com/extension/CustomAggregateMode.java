package com.extension;

import com.rIntegration.Mode;
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
 * Created by Divya on 9/22/2017.
 */
public class CustomAggregateMode{

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("CustomAggregateMode");
    public static ArrayList<Double> arrayList = new ArrayList<>();

    static AccumulatorFactory modeAccumulatorFactory = new AccumulatorFactory() {
        @Override
        public Accumulator createAccumulator(AggCustom agg, boolean distinct) {
            return new ModeAccumulator(agg);
        }
    };

    static {
        String aggUri = "http://example.org/function#mode" ;
        AggregateRegistry.register(aggUri,modeAccumulatorFactory, NodeConst.nodeMinusOne);
    }

    public static void init(){
        LOGGER.info("****************** INITIATED CUSTOMAGGREGATEMODE **********************");
        String aggUri = "http://example.org/function#mode" ;
        AggregateRegistry.register(aggUri,modeAccumulatorFactory, NodeConst.nodeMinusOne);
    }

    static class ModeAccumulator implements Accumulator{
        double modeValue = 0;
        private AggCustom aggCustom;

        ModeAccumulator(AggCustom aggCustom){
            this.aggCustom = aggCustom;
        }

        @Override
        public void accumulate(Binding binding, FunctionEnv functionEnv) {
            ExprList exprList = aggCustom.getExprList();
            for (Expr expr :
                    exprList) {
                try {
                    NodeValue nv = expr.eval(binding,functionEnv);
                    if(nv.isLiteral()){
                        arrayList.add(Double.parseDouble(nv.asUnquotedString()));
                    }
                }catch (ExprEvalException ex){
                    ex.printStackTrace();
                }
            }
        }

        @Override
        public NodeValue getValue() {
            Mode mode = new Mode();
            modeValue = mode.getMode(arrayList);
            DecimalFormat decimalFormat = new DecimalFormat("#.#####");
            double result = Double.parseDouble(decimalFormat.format(modeValue));
            return NodeValue.makeDouble(result);
        }
    }
}
