package com.extension;

import com.rIntegration.OneSampleTtest;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import org.apache.jena.sparql.function.FunctionRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * Created by Divya on 8/31/2017.
 */
public class OneSamplePTest extends FunctionBase3{

    static {
        FunctionRegistry.get().put("http://example.org/function#onePTest",OneSamplePTest.class);
    }

    public static void init(){
        FunctionRegistry.get().put("http://example.org/function#onePTest",OneSamplePTest.class);
    }

    public double getOneSamplePStatistic(String doubleString, double mean, int size){
        OneSampleTtest oneSamplePtest = new OneSampleTtest();
        double pTestStatistic = oneSamplePtest.getOneSamplePStatistic(doubleString,mean, size);
        return pTestStatistic;
    }

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {
        double pStatistic = getOneSamplePStatistic(v1.asUnquotedString(),v2.getDouble(), v3.getInteger().intValue());
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        double result = Double.parseDouble(decimalFormat.format(pStatistic));
        return NodeValue.makeDecimal(result);
    }

    public static void main(String[] args) throws FileNotFoundException {
        OneSamplePTest oneSamplePTest = new OneSamplePTest();

        String qs = "PREFIX fn:<http://example.org/function#> SELECT (fn:onePTest(group_concat(?o;separator=','),10) AS ?pstatistic) " +
                "{?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;
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
