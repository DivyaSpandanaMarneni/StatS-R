package com.rIntegration;


import org.rosuda.JRI.Rengine;

import java.util.ArrayList;


public class Skewness {

    public double getSkewness(ArrayList<Double> inputList){
        double skewness = 0;
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("c(");

        for(int i=0;i<inputList.size();i++){
            stringBuilder.append(Double.toString(inputList.get(i)));

            if(i<inputList.size()-1){
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(")");

        String jVector = stringBuilder.toString();
        try{
            rengine.eval("library(moments,lib.loc=\"C:/Program Files/R/R-3.3.3/library\")");
            rengine.eval("rVector="+jVector);
            rengine.eval("rSkewness=skewness(rVector,na.rm=TRUE)");
            skewness = rengine.eval("rSkewness").asDouble();

        }catch (Exception e){
            e.printStackTrace();
        }

        if(rengine != null)
            rengine.end();
        return skewness;

    }

    public static void main(String[] args){
        ArrayList<Double> arrayList = new ArrayList<>();

        arrayList.add(23.4);
        arrayList.add(28.4);
        arrayList.add(30.4);
        arrayList.add(80.4);
        arrayList.add(100.4);
        arrayList.add(40.4);

        Skewness skewness = new Skewness();
        double skewnessValue = skewness.getSkewness(arrayList);
        System.out.println("The skewness value is : "+skewnessValue);
    }
}
