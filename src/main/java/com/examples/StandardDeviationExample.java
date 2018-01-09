package com.examples;

import com.extension.CustomAggregateSD;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Divya on 9/1/2017.
 */
public class StandardDeviationExample {
    public static void main(String[] args) throws FileNotFoundException {
        CustomAggregateSD customAggregateSD = new CustomAggregateSD();
        String qs = "PREFIX fn:<http://example.org/function#> \n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "SELECT (fn:sd(?o) AS ?standardDeviation)\n" +
                "Where{\n" +
                "?s <http://chronicdata.cdc.gov/resource/g4ie-h725/question> \"Alcohol use among youth\" .  \n" +
                "?s <http://chronicdata.cdc.gov/resource/g4ie-h725/datavalue> ?o .\n" +
                "filter(regex(?o,'[0-9.]')) .\n" +
                "}" ;

        String qsTest = "SELECT ?s ?p ?o where {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o } ";
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
