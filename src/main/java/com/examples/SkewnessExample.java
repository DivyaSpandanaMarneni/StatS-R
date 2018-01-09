package com.examples;

import com.extension.CustomAggregateMean;
import com.extension.CustomAggregateSkewness;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Divya on 9/2/2017.
 */
public class SkewnessExample {
    public static void main(String[] args) throws FileNotFoundException {
        CustomAggregateSkewness customAggregateSkewness = new CustomAggregateSkewness();
        String qs = "PREFIX fn:<http://example.org/function#> \n" +
                "SELECT (fn:skewness(?o) as ?skewness)\n" +
                "\tWhere{\n" +
                "\t?s <http://chronicdata.cdc.gov/resource/g4ie-h725/question> \"Binge drinking prevalence among adults aged >= 18 years\" .  \n" +
                " \t?s <http://chronicdata.cdc.gov/resource/g4ie-h725/datavalue> ?o . \t \n" +
                "\tfilter(regex(?o,'[0-9.]')) .\n" +
                "\t}" ;
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
