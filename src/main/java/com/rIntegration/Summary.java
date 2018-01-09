package com.rIntegration;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import java.util.ArrayList;

/**
 * Created by Divya on 7/16/2017.
 */
public class Summary {

    public void getSummary(ArrayList<Double> arrayList){
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("c(");
        for(int i=0;i<arrayList.size();i++){
            stringBuilder.append(Double.toString(arrayList.get(i)));
            if(i != arrayList.size()-1)
                stringBuilder.append(",");
        }
        stringBuilder.append(")");

        String jVector = stringBuilder.toString();

        rengine.eval("rVector="+jVector);
        rengine.eval("rSummary=summary(rVector)");

        REXP rsummary = rengine.eval("rSummary");
        System.out.println("The summary is : ");
        System.out.println(rsummary.toString());

        //System.out.println("The standard deviation is "+sd);
        if(rengine != null)
            rengine.end();

        //return var;
    }

    public static void main(String[] args){

        ArrayList<Double> arrayList = new ArrayList<>();

        arrayList.add(23.4);
        arrayList.add(28.4);
        arrayList.add(30.4);
        arrayList.add(80.4);
        arrayList.add(100.4);
        arrayList.add(40.4);

        Summary summary = new Summary();
        summary.getSummary(arrayList);
    }
}
