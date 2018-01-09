package com.examples;

import com.extension.CustomAggregateMean;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class MeanExample {

    private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("MeanExample");

    public MeanExample() {

    }

    public static void main(String[] args) throws FileNotFoundException {
        LOGGER.info("*******************************************");
        System.out.println("****************************************************");
        CustomAggregateMean customAggregateMean = new CustomAggregateMean();
        //String qs = "SELECT (<http://example/getMean>(?o) AS ?mean) {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;
        String qs = "PREFIX fn:<http://example.org/function#> \n" +
                "SELECT (fn:mean(?o) AS ?mean) \n" +
                "{?s <https://chronicdata.cdc.gov/resource/_4ny5-qn3w/indicator> \"Prevalence of major cardiovascular disease among US adults (18+); BRFSS\" .\n" +
                "  ?s <https://chronicdata.cdc.gov/resource/_4ny5-qn3w/data_value> ?o\n" +
                "}\n" ;
        //String qs = "PREFIX fn:<java:com.extension.> SELECT (fn:CustomAggregateMean(?o) AS ?mean) {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;

        String qsTest = "SELECT ?s ?p ?o where {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o } ";


        Model model = ModelFactory.createDefaultModel();
        String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\CardiovascularFrom2011.rdf";
        FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
        model.read(fileInputStream,null);

        Dataset dataset = DatasetFactory.create(model);
        List<Double> list = new ArrayList<>();
        Literal result = null;

        Query q = QueryFactory.create(qs) ;
        try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(rs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
