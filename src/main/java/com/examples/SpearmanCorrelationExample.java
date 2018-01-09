package com.examples;

import com.extension.SpearmanCorrelation;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Divya on 9/1/2017.
 */
public class SpearmanCorrelationExample {
    public static void main(String[] args) throws FileNotFoundException {
        SpearmanCorrelation spearmanCorrelation = new SpearmanCorrelation();

        String queryString ="PREFIX fn:<http://example.org/function#> Select (fn:spearmancorr(  group_concat(?o1;separator=','), group_concat(?o2;separator=','))  AS ?spearmanCorrelation)\n" +
                "where {\n" +
                "?s  <http://chronicdata.cdc.gov/resource/g4ie-h725/datavalue> ?o1 .\n" +
                "?s  <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o2 .\n" +
                "}";



        Model model = ModelFactory.createDefaultModel();
        String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\ChronicDiseases.rdf";
        FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
        model.read(fileInputStream,null);

        Dataset dataset = DatasetFactory.create(model);

        Query q = QueryFactory.create(queryString) ;
        try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(rs);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
