package com.rIntegration;

import org.rosuda.JRI.Rengine;

import java.util.ArrayList;

/**
 * Created by Divya on 7/9/2017.
 */
public class Mean {

    public double getMean(ArrayList<Double> list){
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("c(");
        for(int i = 0;i<list.size();i++){
            stringBuilder.append(Double.toString(list.get(i)));
            if(i<list.size()-1)
                stringBuilder.append(",");

        }
        stringBuilder.append(")");

        String jVector = stringBuilder.toString();
        rengine.eval("rVector="+jVector);
        rengine.eval("rMean=mean(rVector,na.rm=TRUE)");
        double mean = rengine.eval("rMean").asDouble();
        if(rengine != null)
            rengine.end();
        return mean;
    }

    public static void main(String[] args){

        ArrayList<Double> arrayList = new ArrayList<>();

        arrayList.add(23.4);
        arrayList.add(28.4);
        arrayList.add(30.4);
        arrayList.add(80.4);
        arrayList.add(100.4);
        arrayList.add(40.4);

        Mean mean = new Mean();
        int meanValue = (int) mean.getMean(arrayList);
        System.out.println("The mean value is "+meanValue);

    }
}
