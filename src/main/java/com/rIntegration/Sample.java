package com.rIntegration;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 11/18/2017.
 */
public class Sample {
    public List<Object> getSample(String doubleList, int sampleSize){
        doubleList = doubleList.replaceAll(", ,",",");
        doubleList = doubleList.replaceAll(",,",",");
        List<Object> resultSample = new ArrayList<>();

        String[] doubleListForLen = doubleList.split(",");
        if(doubleListForLen.length <= sampleSize){
            for (String st :
                    doubleListForLen) {
                resultSample.add(st);
            }
            return resultSample;
        }
        StringBuilder input = new StringBuilder();
        input.append("c(");
        input.append(doubleList);
        input.append(")");

        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        rengine.eval("rInputVector=as.numeric("+input.toString()+")");
        rengine.eval("sampleList=sample(rInputVector,size="+sampleSize+")");
        REXP result = rengine.eval("sampleList");
        double[] res = result.asDoubleArray();
        for(int i=0;i<res.length;i++){
            resultSample.add(res[i]);
        }

        return resultSample;
    }

    public static void main(String[] args){
        Sample sample = new Sample();
        List<Object> result = sample.getSample("1,2,3,4,5,,7",3);
        for (Object res :
                result) {
            System.out.println("Here value is "+res);
        }
    }
}
