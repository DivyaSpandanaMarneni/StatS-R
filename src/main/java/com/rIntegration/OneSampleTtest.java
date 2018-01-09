package com.rIntegration;

import org.rosuda.JRI.Rengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 8/17/2017.
 */
public class OneSampleTtest {

    public double getTStatistic(ArrayList<Double> arrayList, double mean){

        StringBuilder stringBuilder = new StringBuilder();
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        stringBuilder.append("c(");

        for(int i=0;i<arrayList.size();i++){
            stringBuilder.append(arrayList.get(i).toString());
            if(i != arrayList.size()-1)
                stringBuilder.append(",");
        }
        stringBuilder.append(")");

        String jVector = stringBuilder.toString();
        rengine.eval("rVector="+jVector);
        rengine.eval("ttest=t.test(rVector,mu="+mean+")");
        double result = rengine.eval("ttest[['statistic']]").asDouble();

        if(rengine != null)
            rengine.end();
        return result;
    }

    public double getTStatistic(String doubleList, double mean, int size){

        StringBuilder stringBuilder = new StringBuilder();
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        stringBuilder.append("c(");
        Sample sample = new Sample();
        List<Object> sampleSubset = sample.getSample(doubleList,size);

        for(int i=0;i<sampleSubset.size(); i++){
            stringBuilder.append(sampleSubset.get(i));
            stringBuilder.append(",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        //stringBuilder.append(doubleList);
        stringBuilder.append(")");

        String jVector = stringBuilder.toString();
        rengine.eval("rVector="+jVector);
        rengine.eval("ttest=t.test(rVector,mu="+mean+")");
        double result = rengine.eval("ttest[['statistic']]").asDouble();

        if(rengine != null)
            rengine.end();
        return result;
    }

    public double getOneSamplePStatistic(String doubleList, double mean, int size){
        StringBuilder stringBuilder = new StringBuilder();
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        stringBuilder.append("c(");
        Sample sample = new Sample();
        List<Object> sampleSubset = sample.getSample(doubleList,size);

        for(int i=0;i<sampleSubset.size(); i++){
            stringBuilder.append(sampleSubset.get(i));
            stringBuilder.append(",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        //stringBuilder.append(doubleList);
        stringBuilder.append(")");

        String jVector = stringBuilder.toString();
        rengine.eval("rVector="+jVector);
        rengine.eval("ttest=t.test(rVector,mu="+mean+")");
        double result = rengine.eval("ttest[['p.value']]").asDouble();

        if(rengine != null)
            rengine.end();
        return result;
    }

    public static void main(String[] args){
        OneSampleTtest oneSampleTtest = new OneSampleTtest();
        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(23.4);
        arrayList.add(28.4);
        arrayList.add(30.4);
        arrayList.add(80.4);
        arrayList.add(100.4);
        arrayList.add(50.4);
        arrayList.add(60.4);
        arrayList.add(70.6);
        arrayList.add(90.5);

        //System.out.println("The ttest result is "+oneSampleTtest.getTStatistic(arrayList,30,5));



    }
}
