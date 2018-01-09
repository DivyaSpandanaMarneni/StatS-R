package com.examples;

import com.extension.OneSampleTTestUDF;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Divya on 9/1/2017.
 */
public class OneSampleTTestExample {

    public static void main(String[] args) throws FileNotFoundException {
        OneSampleTTestUDF oneSampleTTestUDF = new OneSampleTTestUDF();

        String qs = "\n" +
                "PREFIX fn:<http://example.org/function#>\n" +
                "select (fn:oneTTest(group_concat(?o;separator=','),10.10714,13) as ?tstatistic)\n" +
                "where{\n" +
                " ?s <https://chronicdata.cdc.gov/resource/_5svk-8bnq/indicator> \"Prevalence of major cardiovascular disease among US adults (20+); NHANES\" .\n" +
                " ?s <https://chronicdata.cdc.gov/resource/_5svk-8bnq/year> \"2003-2004\" .\n" +
                " ?s <https://chronicdata.cdc.gov/resource/_5svk-8bnq/data_value> ?o\n" +
                "}\n" ;
        Model model = ModelFactory.createDefaultModel();
        String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\NutritionExaminationSurvey.rdf";
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
