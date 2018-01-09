package com.extension;

import com.rIntegration.TwoSampleZTest;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionRegistry;

import java.text.DecimalFormat;

/**
 * Created by Divya on 12/16/2017.
 */
public class TwoSampleZTestUDF extends FunctionBase5 {

    static {
        FunctionRegistry.get().put("http://example.org/function#twoZTest",TwoSampleZTestUDF.class);
    }

    public static void init(){
        FunctionRegistry.get().put("http://example.org/function#twoZTest",TwoSampleZTestUDF.class);
    }

    public double getTwoSampleZStatistic(String doubleString1, String doubleString2, double variance1,double variance2, int size){
        TwoSampleZTest twoSampleZTest = new TwoSampleZTest();
        String[] sample1 = doubleString1.split(":");
        String[] sample2 = doubleString2.split(":");
        if(sample1.length < size || sample2.length < size)
            size = Math.min(sample1.length,sample2.length);
        double zTestStatistic = twoSampleZTest.getTwoSampleZTest(doubleString1,doubleString2,variance1,variance2,size);
        return zTestStatistic;
    }

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3, NodeValue v4, NodeValue v5) {
        double zStatistic = getTwoSampleZStatistic(v1.asUnquotedString(),v2.asUnquotedString(), v3.getDouble(), v4.getDouble(), v5.getInteger().intValue());
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        double result = Double.parseDouble(decimalFormat.format(zStatistic));
        return NodeValue.makeDecimal(result);
    }
}
