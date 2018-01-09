package com.extension;

import com.rIntegration.Variance;
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


public class CustomAggregateVariance {
    public static ArrayList<Double> arrayList = new ArrayList<>();

    static AccumulatorFactory myAccumulatorFacotry = new AccumulatorFactory() {
        @Override
        public Accumulator createAccumulator(AggCustom agg, boolean distinct) {
            return new VarAccumulator(agg);
        }
    };

    static {
        String aggUri = "http://example.org/function#variance" ;
        AggregateRegistry.register(aggUri,myAccumulatorFacotry, NodeConst.nodeMinusOne);
    }

    public static void init(){
        String aggUri = "http://example.org/function#variance" ;
        AggregateRegistry.register(aggUri,myAccumulatorFacotry, NodeConst.nodeMinusOne);
    }

    static class VarAccumulator implements Accumulator{
        double var=0;
        private AggCustom aggCustom;

        VarAccumulator(AggCustom aggCustom){
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
                    ex.printStackTrace();
                }
            }
        }

        @Override
        public NodeValue getValue() {
            Variance variance = new Variance();
            var = variance.getVariance(arrayList);
            DecimalFormat decimalFormat = new DecimalFormat("#.#####");
            double result = Double.parseDouble(decimalFormat.format(var));
            return NodeValue.makeDecimal(result);
        }
    }


}
