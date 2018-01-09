package com.rIntegration;

import org.rosuda.JRI.Rengine;

import java.util.Arrays;

/**
 * Created by Divya on 8/26/2017.
 */
public class Correlation  {

    public double getPearsonCorrelation(String input1, String input2){
        double correlationValue =0;
        StringBuilder stringBuilderInput1 = new StringBuilder();
        StringBuilder stringBuilderInput2 = new StringBuilder();
        String[] inputArr1 = input1.split(",");
        String[] inputArr2 = input2.split(",");
        int len = 0;
        if(inputArr1.length < inputArr2.length)
            len = inputArr1.length;
        else
            len = inputArr2.length;

        String[] inputArr1Dst = Arrays.copyOf(inputArr1,len);
        String[] inputArr2Dst = Arrays.copyOf(inputArr2,len);

        input1 =  Arrays.toString(inputArr1Dst);
        input2 = Arrays.toString(inputArr2Dst);
        input1 = input1.substring(1,input1.length()-1);
        input2 = input2.substring(1, input2.length()-1);
        stringBuilderInput1.append("c(");
        stringBuilderInput2.append("c(");
        stringBuilderInput1.append(input1);
        stringBuilderInput2.append(input2);
        stringBuilderInput1.append(")");
        stringBuilderInput2.append(")");

        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }
        rengine.eval("rXVector=as.numeric("+stringBuilderInput1.toString()+")");
        rengine.eval("rYVector=as.numeric("+stringBuilderInput2.toString()+")");
        rengine.eval("result=cor(rXVector,rYVector,method=c('pearson'))");
        correlationValue = rengine.eval("result").asDouble();

        if(rengine!= null)
            rengine.end();
        return correlationValue;
    }

    public double getKendallCorrelation(String input1, String input2){
        double correlationValue =0;
        StringBuilder stringBuilderInput1 = new StringBuilder();
        StringBuilder stringBuilderInput2 = new StringBuilder();

        stringBuilderInput1.append("c(");
        stringBuilderInput2.append("c(");
        stringBuilderInput1.append(input1);
        stringBuilderInput2.append(input2);
        stringBuilderInput1.append(")");
        stringBuilderInput2.append(")");

        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        rengine.eval("rXVector=as.numeric("+stringBuilderInput1.toString()+")");
        rengine.eval("rYVector=as.numeric("+stringBuilderInput2.toString()+")");

        rengine.eval("result=cor(rXVector,rYVector,method=c('kendall'))");

        correlationValue = rengine.eval("result").asDouble();

        if(rengine!= null)
            rengine.end();

        return correlationValue;
    }

    public double getSpearmanCorrelation(String input1, String input2){
        double correlationValue =0;
        StringBuilder stringBuilderInput1 = new StringBuilder();
        StringBuilder stringBuilderInput2 = new StringBuilder();

        stringBuilderInput1.append("c(");
        stringBuilderInput2.append("c(");
        stringBuilderInput1.append(input1);
        stringBuilderInput2.append(input2);
        stringBuilderInput1.append(")");
        stringBuilderInput2.append(")");

        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null){
            rengine = new Rengine(new String[] {"--no-save"},false,null);
        }

        rengine.eval("rXVector=as.numeric("+stringBuilderInput1.toString()+")");
        rengine.eval("rYVector=as.numeric("+stringBuilderInput2.toString()+")");

        rengine.eval("result=cor(rXVector,rYVector,method=c('spearman'))");

        correlationValue = rengine.eval("result").asDouble();

        if(rengine!= null)
            rengine.end();

        return correlationValue;
    }

    
}
