package com.extension;

import com.rIntegration.Correlation;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.function.FunctionRegistry;

import java.text.DecimalFormat;

/**
 * Created by Divya on 8/26/2017.
 */
public class SpearmanCorrelation extends FunctionBase2{

    static {
        FunctionRegistry.get().put("http://example.org/function#spearmancorr",SpearmanCorrelation.class);
    }

    public static void init(){
        FunctionRegistry.get().put("http://example.org/function#spearmancorr",SpearmanCorrelation.class);
    }

    public double getSpearmanCorrelation(String input1, String input2){
        Correlation correlation = new Correlation();
        return correlation.getSpearmanCorrelation(input1,input2);
    }

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
        double spearmanCorrelationVal = getSpearmanCorrelation(v1.asUnquotedString(),v2.asUnquotedString());
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        double result = Double.parseDouble(decimalFormat.format(spearmanCorrelationVal));
        return NodeValue.makeDecimal(result);
    }


}
