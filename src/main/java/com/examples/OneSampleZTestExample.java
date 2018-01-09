package com.examples;

import com.extension.OneSampleZTestUDF;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Divya on 12/13/2017.
 */
public class OneSampleZTestExample {
    public static void main(String[] args) throws FileNotFoundException {
        OneSampleZTestUDF oneSampleZTestUDF = new OneSampleZTestUDF();

//        String qs = "PREFIX fn:<http://example.org/function#> SELECT (fn:oneZTest(group_concat(?o;separator=','),10.88648,145.67908,50) AS ?zstatistic) " +
//                "{?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;
        String qs ="PREFIX fn:<http://example.org/function#> \n" +
                "SELECT (fn:oneZTest(group_concat(?o;separator=','),17.57692,17.75583,30) AS ?zstatistic) \n" +
                "where {\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/locationabbr> \"GA\" .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/year> \"2014-2015\"  .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/topicdesc> \"Cigarette Use (Adults)\" .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/data_value> ?o .\t\n" +
                "filter(regex(?o ,'[0-9.]')) \n" +
                "} ";
        Model model = ModelFactory.createDefaultModel();
        //String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\ChronicDiseases.rdf";
        String fileLocation = "V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\TobaccoUse.rdf";
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
