package com.extension;

import com.rIntegration.Sample;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.function.FunctionRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 11/17/2017.
 */
public class TestSample extends FunctionBase2{
    static {
        FunctionRegistry.get().put("http://example.org/function#sample",TestSample.class);
    }

    public static void init(){
        FunctionRegistry.get().put("http://example.org/function#sample",TestSample.class);
    }

    //r function to get sample
    public List<NodeValue> getSample(String inputSample, int size){
        List<NodeValue> resultNodes = new ArrayList<>();
        Sample sample = new Sample();
        List<Object> resultList = sample.getSample(inputSample,size);
        for (Object ob :
                resultList) {
            //resultNodes.add(NodeValue.makeDecimal(ob));
        }
        return resultNodes;
    }


    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
        return null;
    }
}
