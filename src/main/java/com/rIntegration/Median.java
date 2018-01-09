package com.rIntegration;

import org.rosuda.JRI.Rengine;

import java.util.ArrayList;

/**
 * Created by Divya on 8/13/2017.
 */
public class Median {
    public double getMedian(ArrayList<Double> arrayList){
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("c(");

        for(int i=0;i<arrayList.size();i++){
            stringBuilder.append(Double.toString(arrayList.get(i)));

            if(i<arrayList.size()-1){
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(")");

        String jVector = stringBuilder.toString();
        rengine.eval("rVector="+jVector);
        rengine.eval("rMedian=median(rVector,na.rm=TRUE)");
        double median = rengine.eval("rMedian").asDouble();
        if(rengine != null)
            rengine.end();
        return median;

    }

    public static void main(String[] args){
        ArrayList<Double> arrayList = new ArrayList<>();

        arrayList.add(23.4);
        arrayList.add(28.4);
        arrayList.add(30.4);
        arrayList.add(80.4);
        arrayList.add(100.4);
        arrayList.add(40.4);

        Median median = new Median();
        double medianValue = median.getMedian(arrayList);
        System.out.println("The median value is : "+medianValue);
    }
}
