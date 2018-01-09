package com.rIntegration;

import org.rosuda.JRI.Rengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 8/18/2017.
 */
public class TwoSampleTTest {
    public double getTwoSampleTTest(ArrayList<Double> arrayList1, ArrayList<Double> arrayList2){
        StringBuilder sbSample1 = new StringBuilder();
        StringBuilder sbSample2 = new StringBuilder();

        sbSample1.append("c(");
        sbSample2.append("c(");

        for(int i=0;i<arrayList1.size();i++){
            sbSample1.append(arrayList1.get(i).toString());
            if(i != arrayList1.size()-1)
                sbSample1.append(",");
        }
        sbSample1.append(")");

        for(int i=0;i<arrayList2.size();i++){
            sbSample2.append(arrayList2.get(i).toString());
            if(i != arrayList2.size()-1)
                sbSample2.append(",");
        }

        sbSample2.append(")");

        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        rengine.eval("rVectorSample1="+sbSample1.toString());
        rengine.eval("rVectorSample2="+sbSample2.toString());

        rengine.eval("ttest=t.test(rVectorSample1,rVectorSample2)");
        double twoTestResult = rengine.eval("ttest[['statistic']]").asDouble();

        if(rengine!=null)
            rengine.end();

        return twoTestResult;
    }

    public double getTwoSampleTTest(String  concatSample1, String concatSample2, int size){
        StringBuilder sbSample1 = new StringBuilder();
        StringBuilder sbSample2 = new StringBuilder();

        concatSample1 = concatSample1.replaceAll(":",",");
        concatSample2 = concatSample2.replaceAll(":",",");

        Sample sample = new Sample();
        List<Object> sampleSubset = sample.getSample(concatSample1,size);

        sbSample1.append("c(");
        for(int i=0;i<sampleSubset.size(); i++){
            sbSample1.append(sampleSubset.get(i));
            sbSample1.append(",");
        }
        //sbSample1.append(concatSample1);
        sbSample1.deleteCharAt(sbSample1.length()-1);
        sbSample1.append(")");

        sampleSubset = sample.getSample(concatSample2,size);
        sbSample2.append("c(");
        for(int i=0;i<sampleSubset.size(); i++){
            sbSample2.append(sampleSubset.get(i));
            sbSample2.append(",");
        }
        //sbSample1.append(concatSample1);
        sbSample2.deleteCharAt(sbSample2.length()-1);
        //sbSample2.append(concatSample2);
        sbSample2.append(")");



        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        rengine.eval("rVectorSample1="+sbSample1.toString());
        rengine.eval("rVectorSample2="+sbSample2.toString());

        rengine.eval("ttest=t.test(rVectorSample1,rVectorSample2)");
        double twoTestResult = rengine.eval("ttest[['statistic']]").asDouble();

        if(rengine!=null)
            rengine.end();

        return twoTestResult;
    }

    public double getTwoSamplePTest(String concatSample1, String concatSample2){
        StringBuilder sbSample1 = new StringBuilder();
        StringBuilder sbSample2 = new StringBuilder();

        sbSample1.append("c(");
        sbSample1.append(concatSample1);
        sbSample1.append(")");
        sbSample2.append("c(");
        sbSample2.append(concatSample2);
        sbSample2.append(")");



        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        rengine.eval("rVectorSample1="+sbSample1.toString());
        rengine.eval("rVectorSample2="+sbSample2.toString());

        rengine.eval("ttest=t.test(rVectorSample1,rVectorSample2)");
        double twoTestResult = rengine.eval("ttest[['p.value']]").asDouble();

        if(rengine!=null)
            rengine.end();

        return twoTestResult;
    }

    public static void main(String[] args){

        ArrayList<Double> arrayList1 = new ArrayList<>();
        arrayList1.add(10.0);
        arrayList1.add(20.1);
        arrayList1.add(30.1);
        arrayList1.add(40.1);
        arrayList1.add(50.1);
        arrayList1.add(60.1);

        ArrayList<Double> arrayList2 = new ArrayList<>();
        arrayList2.add(20.1);
        arrayList2.add(30.1);
        arrayList2.add(40.1);
        arrayList2.add(50.1);
        arrayList2.add(60.1);

        TwoSampleTTest twoSampleTTest = new TwoSampleTTest();
        double tTestStatistic = twoSampleTTest.getTwoSampleTTest(arrayList1,arrayList2);
        System.out.println("The result is "+tTestStatistic);


    }
}
