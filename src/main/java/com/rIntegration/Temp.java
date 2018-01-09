package com.rIntegration;

import org.rosuda.JRI.Rengine;

/**
 * Created by Divya on 4/16/2017.
 */
public class Temp {
    public static void main(String[] args){
        String javaVector = "c(1,2,3,4)";
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null)
        rengine = new Rengine(new String[] {"--no-save"},false,null);
        //Rengine rengine = new Rengine();

        rengine.eval("rVector="+javaVector);

        //calculate mean of the vector using R syntax
        rengine.eval("meanVal=mean(rVector)");

        double mean = rengine.eval("meanVal").asDouble();

        System.out.println("Mean of the given vector is="+mean);

        rengine.end();

    }



    public Double getMean(){

        String javaVector = "c(1,2,3,4)";
        Rengine rengine = Rengine.getMainEngine();
        if(rengine == null)
        rengine = new Rengine(new String[] {"--save"},false,null);
        //Rengine rengine = new Rengine();

        rengine.eval("rVector="+javaVector);

        //calculate mean of the vector using R syntax
        rengine.eval("meanVal=mean(rVector)");

        double mean = rengine.eval("meanVal").asDouble();

        System.out.println("Mean of the given vector is="+mean);
        if(rengine!=null)
        rengine.end();
        return mean;

    }
}
