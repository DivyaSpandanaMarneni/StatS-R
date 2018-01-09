package com.rIntegration;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import java.util.List;

/**
 * Created by Divya on 12/13/2017.
 */
public class OneSampleZTest {
    public double getZTestValue(String doubleList, double mean, double variance, int size){
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

        String ztestFunction = "function(a, mu, var){\n" +
                "   zeta = (mean(a) - mu) / (sqrt(var / length(a)))\n" +
                "   return(zeta)\n" +
                "}";

        rengine.eval("z.test="+ztestFunction);
        REXP zValueResult = rengine.eval("z.test("+stringBuilder+","+mean+","+variance+")");
        return zValueResult.asDouble();
    }

    public double getPFromZscore(String doubleList,double mean, double variance, int size){
        double zscore = getZTestValue(doubleList,mean,variance,size);
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        rengine.eval("p.value=2*pnorm(-abs("+zscore+")");
        double pvalue = rengine.eval("p.value").asDouble();
        return pvalue;
    }

    public static void main(String[] args){
        String dbl1 = "65,78,88,55,48,95,66,57,79,81";
        String dbl2 = "185,169,173,173,188,186,175,174,179,180";
        double var1 = 5;
        double var2 = 8.5;

        OneSampleZTest oneSampleZTest = new OneSampleZTest();
        double result = oneSampleZTest.getZTestValue(dbl1,75.0, 18.0,10);
        System.out.println("the final result "+result);
    }
}
