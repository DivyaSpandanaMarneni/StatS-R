package com.extension;

import com.rIntegration.OneSampleZTest;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase4;
import org.apache.jena.sparql.function.FunctionRegistry;

import java.text.DecimalFormat;

/**
 * Created by Divya on 12/13/2017.
 */
public class OneSampleZTestUDF extends FunctionBase4 {
    static {
        FunctionRegistry.get().put("http://example.org/function#oneZTest",OneSampleZTestUDF.class);
    }

    public static void init(){
        FunctionRegistry.get().put("http://example.org/function#oneZTest",OneSampleZTestUDF.class);
    }

    public double getOneSampleZStatistic(String doubleString, double mean,double variance, int size){
        OneSampleZTest oneSampleZTest = new OneSampleZTest();
        double zTestStatistic = oneSampleZTest.getZTestValue(doubleString,mean,variance,size);
        return zTestStatistic;
    }

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3, NodeValue v4) {
        double zStatistic = getOneSampleZStatistic(v1.asUnquotedString(),v2.getDouble(), v3.getDouble(), v4.getInteger().intValue());
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        double result = Double.parseDouble(decimalFormat.format(zStatistic));
        return NodeValue.makeDecimal(result);
    }
}
