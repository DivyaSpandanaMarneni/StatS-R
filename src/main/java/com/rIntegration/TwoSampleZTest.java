package com.rIntegration;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import java.util.List;

/**
 * Created by Divya on 12/13/2017.
 */
public class TwoSampleZTest {
    public double getTwoSampleZTest(String doubleList1, String doubleList2, double variance1, double variance2, int size){
        try {
            Rengine rengine = Rengine.getMainEngine();
            if (rengine == null) {
                rengine = new Rengine(new String[]{"--no-save"}, false, null);
            }
            StringBuilder sbSample1 = new StringBuilder();
            StringBuilder sbSample2 = new StringBuilder();
            doubleList1 = doubleList1.replaceAll(":",",");
            doubleList2 = doubleList2.replaceAll(":",",");
            Sample sample = new Sample();
            sbSample1.append("c(");
            List<Object> sampleSubset = sample.getSample(doubleList1, size);

            for (int i = 0; i < sampleSubset.size(); i++) {
                sbSample1.append(sampleSubset.get(i));
                sbSample1.append(",");
            }

            sbSample1.deleteCharAt(sbSample1.length() - 1);
            sbSample1.append(")");

            sbSample2.append("c(");
            sampleSubset = sample.getSample(doubleList2, size);

            for (int i = 0; i < sampleSubset.size(); i++) {
                sbSample2.append(sampleSubset.get(i));
                sbSample2.append(",");
            }
            sbSample2.deleteCharAt(sbSample2.length() - 1);
            sbSample2.append(")");

            String zTestTwoSampleFunction = "function(a, b, var.a, var.b){\n" +
                    "   n.a = length(a)\n" +
                    "   n.b = length(b)\n" +
                    "   zeta = (mean(a) - mean(b)) / (sqrt(var.a/n.a + var.b/n.b))\n" +
                    "   return(zeta)}";

            rengine.eval("z.test2=" + zTestTwoSampleFunction);
            REXP zValueResult = rengine.eval("z.test2(" + sbSample1 + "," + sbSample2 + "," + variance1 + "," + variance2 + ")");
            double zValue = zValueResult.asDouble();

            return zValue;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }




    public static void main(String[] args){
        String dbl1 = "175,168,168,190,156,181,182,175,174,179";
        String dbl2 = "185,169,173,173,188,186,175,174,179,180";
        double var1 = 5;
        double var2 = 8.5;

        TwoSampleZTest twoSampleZTest = new TwoSampleZTest();
        double result = twoSampleZTest.getTwoSampleZTest(dbl1,dbl2,var1,var2,10);
        System.out.println("the final result "+result);
    }
}
