package com.extension;

import com.rIntegration.TwoSampleTTest;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.function.FunctionRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * Created by Divya on 8/31/2017.
 */
public class TwoSamplePTest extends FunctionBase2 {

    static {
        FunctionRegistry.get().put("http://example.org/function#twoPTest",TwoSamplePTest.class);
    }

    public static void init(){
        FunctionRegistry.get().put("http://example.org/function#twoPTest",TwoSamplePTest.class);
    }

    public double getTwoSamplePStatistic(String sample1, String sample2){
        TwoSampleTTest twoSamplePTest = new TwoSampleTTest();
        double result = twoSamplePTest.getTwoSamplePTest(sample1,sample2);
        return result;
    }

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
        double twoPTest = getTwoSamplePStatistic(v1.asUnquotedString(),v2.asUnquotedString());
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        double result = Double.parseDouble(decimalFormat.format(twoPTest));
        return NodeValue.makeDecimal(result);
    }

    public static void main(String[] args) throws FileNotFoundException, Exception {

        String qs = "PREFIX fn:<http://example.org/function#> SELECT (fn:twoPTest(group_concat(?o;separator=','),group_concat(?o;separator=',')) AS ?pstatistic) {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;
        Model model = ModelFactory.createDefaultModel();
        String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\ChronicDiseases.rdf";
        FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
        model.read(fileInputStream,null);

        Dataset dataset = DatasetFactory.create(model);

        Query q = QueryFactory.create(qs) ;
        try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(rs);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
