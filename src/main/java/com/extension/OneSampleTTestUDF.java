package com.extension;

import com.rIntegration.OneSampleTtest;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.apache.jena.sparql.function.FunctionRegistry;

import java.text.DecimalFormat;

/**
 * Created by Divya on 8/23/2017.
 */
public class OneSampleTTestUDF extends FunctionBase3{

    static {
        FunctionRegistry.get().put("http://example.org/function#oneTTest",OneSampleTTestUDF.class);
    }

    public static void init(){
        FunctionRegistry.get().put("http://example.org/function#oneTTest",OneSampleTTestUDF.class);
    }

    public double getOneSampleTStatistic(String doubleString, double mean,int size){
        OneSampleTtest oneSampleTtest = new OneSampleTtest();
        double tTestStatistic = oneSampleTtest.getTStatistic(doubleString,mean, size);
        return tTestStatistic;
    }

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {
        double tStatistic = getOneSampleTStatistic(v1.asUnquotedString(),v2.getDouble(), v3.getInteger().intValue());
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        double result = Double.parseDouble(decimalFormat.format(tStatistic));
        return NodeValue.makeDecimal(result);
    }
}
