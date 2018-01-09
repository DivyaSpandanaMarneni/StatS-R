package com.examples;

import com.extension.CustomAggregateMedian;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Divya on 9/1/2017.
 */
public class MedianExample {
    public static void main(String[] args) throws FileNotFoundException {

        CustomAggregateMedian customAggregateMedian = new CustomAggregateMedian();
        String qs = "PREFIX fn:<http://example.org/function#> \n" +
                "SELECT (fn:median(?o) AS ?median)\n" +
                "Where{\n" +
                "?s <http://chronicdata.cdc.gov/resource/g4ie-h725/question> \"Alcohol use among youth\" .  \n" +
                " ?s <http://chronicdata.cdc.gov/resource/g4ie-h725/datavalue> ?o .\n" +
                "  filter(regex(?o,'[0-9.]'))\n" +
                "}\n" ;
        Model model = ModelFactory.createDefaultModel();
        String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\ChronicDiseases.rdf";
        FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
        model.read(fileInputStream,null);

        Dataset dataset = DatasetFactory.create(model);

        Query query = QueryFactory.create(qs) ;
        try ( QueryExecution qexec = QueryExecutionFactory.create(query, dataset) ) {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(rs);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
