package com.examples;

import com.extension.PearsonCorrelation;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.nodevalue.NodeValueString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Divya on 9/1/2017.
 */
public class PearsonCorrelationExample {
    public static void main(String[] args) throws FileNotFoundException {

        PearsonCorrelation pearsonCorrelation = new PearsonCorrelation();
        StringBuilder sbPopulaltion1 = new StringBuilder();
        StringBuilder sbPopulaltion2 = new StringBuilder();

        String qs1 = "Select ?o1\n" +
                "where{\n" +
                "?s <https://data.cityofnewyork.us/resource/f9bf-2cp4/sat_writing_avg_score> ?o1 .\n" +
                "filter(regex(?o1,'[0-9.]'))\n" +
                "}";

        String qs2 = "Select ?o2\n" +
                "where{\n" +
                "?s <https://data.cityofnewyork.us/resource/f9bf-2cp4/sat_math_avg_score> ?o2 .\n" +
                "filter(regex(?o2,'[0-9.]'))\n" +
                "}";

        Model model = ModelFactory.createDefaultModel();
        String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\CorrelationTestingDatasets\\SatTestResults.rdf";
        String fileLocationTobacco = "V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\HealthQOL.rdf";
        String fileLocationHeart = "V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\CardiovascularFrom2011.rdf";
        FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
        model.read(fileInputStream,null);

        Dataset dataset = DatasetFactory.create(model);

        Query q = QueryFactory.create(qs1) ;
        try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
            ResultSet rs = qexec.execSelect() ;

            while (rs.hasNext()){
                sbPopulaltion1.append(rs.next().getLiteral("?o1"));
                sbPopulaltion1.append(",");
            }
            sbPopulaltion1.deleteCharAt(sbPopulaltion1.length()-1);
            //System.out.println(sbPopulaltion1);
        }catch (Exception e){
            e.printStackTrace();
        }


        Model modelHeart = ModelFactory.createDefaultModel();
        FileInputStream fileInputStreamHeart = new FileInputStream(new File(fileLocation));
        modelHeart.read(fileInputStreamHeart,null);

        Dataset datasetHeart = DatasetFactory.create(modelHeart);
        q = QueryFactory.create(qs2) ;
        try ( QueryExecution qexec = QueryExecutionFactory.create(q, datasetHeart) ) {
            ResultSet rs = qexec.execSelect() ;

            while (rs.hasNext()){
                sbPopulaltion2.append(rs.next().getLiteral("?o2"));
                sbPopulaltion2.append(",");
            }
            sbPopulaltion2.deleteCharAt(sbPopulaltion2.length()-1);
            //System.out.println(sbPopulaltion1);
        }catch (Exception e){
            e.printStackTrace();
        }

        NodeValue n1 = new NodeValueString(sbPopulaltion1.toString());
        NodeValue n2 = new NodeValueString(sbPopulaltion2.toString());

        String qscorr = "PREFIX fn:<http://example.org/function#> \n" +
                "select  (fn:pearsoncorr("+n1+","+n2+")  AS ?pearsonCorrelation)\n" +
                "where{\n" +
                "}";

        q = QueryFactory.create(qscorr);
        try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(rs);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
