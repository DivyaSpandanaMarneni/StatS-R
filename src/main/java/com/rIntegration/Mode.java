package com.rIntegration;

import org.rosuda.JRI.Rengine;

import java.util.ArrayList;

/**
 * Created by Divya on 9/22/2017.
 */
public class Mode {
    public double getMode(ArrayList<Double> arrayList){
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

        rengine.eval("library(modeest,lib.loc=\"C:/Program Files/R/R-3.3.3/library\")");
        rengine.eval("rVector="+jVector);
        rengine.eval("rMode=mlv(rVector,method='mfv')");
        double mode = rengine.eval("rMode").asDouble();
        if(rengine != null)
            rengine.end();
        return mode;

    }

    public static void main(String[] args){
        ArrayList<Double> arrayList = new ArrayList<>();

        arrayList.add(23.4);
        arrayList.add(28.4);
        arrayList.add(30.4);
        arrayList.add(80.4);
        arrayList.add(100.4);
        arrayList.add(40.4);
        arrayList.add(40.4);
        arrayList.add(40.4);

        Mode mode = new Mode();
        double modeValue = mode.getMode(arrayList);
        System.out.println("The mode value is : "+modeValue);
    }
}
