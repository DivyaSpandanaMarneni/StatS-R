package com.extension;

import com.rIntegration.TwoSampleTTest;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.apache.jena.sparql.function.FunctionRegistry;

import java.text.DecimalFormat;

/**
 * Created by Divya on 8/24/2017.
 */
public class TwoSampleTTestUDF extends FunctionBase3 {
    static {
        FunctionRegistry.get().put("http://example.org/function#twoTTest",TwoSampleTTestUDF.class);
    }

    public static void init(){
        FunctionRegistry.get().put("http://example.org/function#twoTTest",TwoSampleTTestUDF.class);
    }

    public double getTwoSampleTStatistic(String sample1, String sample2, int size){
        TwoSampleTTest twoSampleTTest = new TwoSampleTTest();
        String[] sample1Arr = sample1.split(":");
        String[] sample2Arr = sample2.split(":");
        if(sample1Arr.length < size || sample2Arr.length < size)
            size = Math.min(sample1Arr.length,sample2Arr.length);
//        sample1 = sample1.replaceAll("|",",");
//        sample2 = sample2.replaceAll("|",",");
        double result = twoSampleTTest.getTwoSampleTTest(sample1,sample2,size);
        return result;
    }

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {
        double twoSampleTTest = getTwoSampleTStatistic(v1.asUnquotedString(),v2.asUnquotedString(),v3.getInteger().intValue());
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        double result = Double.parseDouble(decimalFormat.format(twoSampleTTest));
        return NodeValue.makeDecimal(result);
    }
}
