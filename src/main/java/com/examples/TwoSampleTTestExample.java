package com.examples;

import com.extension.TwoSampleTTestUDF;
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
public class TwoSampleTTestExample {

    public static void main(String[] args) throws FileNotFoundException {
        TwoSampleTTestUDF twoSampleTTestUDF = new TwoSampleTTestUDF();

        String q1 ="PREFIX fn:<http://example.org/function#>\n" +
                "Select ?o1\n" +
                "where\n" +
                "{\n" +
                "?s <https://chronicdata.cdc.gov/resource/_4juz-x2tp/measuredesc> \"Smoking Status\" .\n" +
                "?s <https://chronicdata.cdc.gov/resource/_4juz-x2tp/locationabbr> \"MO\" .\n" +
                "?s <https://chronicdata.cdc.gov/resource/_4juz-x2tp/data_value> ?o1 .\n" +
                "filter(regex(?o1,'[0-9.]'))\n" +
                "}";

        String q2 = "PREFIX fn:<http://example.org/function#>\n" +
                "Select ?o2\n" +
                "where\n" +
                "{\n" +
                "?s <https://chronicdata.cdc.gov/resource/_4juz-x2tp/measuredesc> \"Smoking Status\" .\n" +
                "?s <https://chronicdata.cdc.gov/resource/_4juz-x2tp/locationabbr> \"GA\" .\n" +
                "?s <https://chronicdata.cdc.gov/resource/_4juz-x2tp/data_value> ?o2 .\n" +
                "filter(regex(?o2,'[0-9.]'))\n" +
                "}";

        StringBuilder sbPopulaltion1 = new StringBuilder();
        StringBuilder sbPopulaltion2 = new StringBuilder();

        //String qs = "PREFIX fn:<http://example.org/function#> SELECT (fn:twoTTest(group_concat(?o;separator=','),group_concat(?o;separator=',')) AS ?tstatistic) {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;
        Model model = ModelFactory.createDefaultModel();
        String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\TobaccoUse.rdf";
        FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
        model.read(fileInputStream,null);
        Dataset dataset = DatasetFactory.create(model);

        String qs = "PREFIX fn:<http://example.org/function#>\n" +
                "SELECT (fn:twoTTest(?oalConcat,?ogaConcat,9) AS ?tstatistic)\n" +
                "where{\n" +
                "{\n" +
                "Select (group_concat(?oal;separator=':') as ?oalConcat) \n" +
                "where{ \n" +
                "{\n" +
                "?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/locationabbr> \"AL\" . \n" +
                "#?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/year> \"2013-2014\"  . \n" +
                "?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/topicdesc> \"Cigarette Use (Adults)\" . \n" +
                "?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/measuredesc> \"Current Smoking – (2 yrs – Race/Ethnicity)\" . \n" +
                "?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/data_value> ?oal .\n" +
                "filter(regex(?oal ,'[0-9.]')) .\n" +
                "}  \t\t\t\t\n" +
                "} \n" +
                "}\n" +
                "{\n" +
                "Select (group_concat(?oga;separator=':') as ?ogaConcat)\n" +
                "where{ \n" +
                "{\n" +
                "?s1 <https://chronicdata.cdc.gov/resource/wsas-xwh5/locationabbr> \"GA\" . \n" +
                "#?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/year> \"2013-2014\"  . \n" +
                "?s1 <https://chronicdata.cdc.gov/resource/wsas-xwh5/topicdesc> \"Cigarette Use (Adults)\" . \n" +
                "?s1 <https://chronicdata.cdc.gov/resource/wsas-xwh5/measuredesc> \"Current Smoking – (2 yrs – Race/Ethnicity)\" .    \t\t\t\t\t  \n" +
                "?s1 <https://chronicdata.cdc.gov/resource/wsas-xwh5/data_value> ?oga .\n" +
                "filter(regex(?oga ,'[0-9.]')) .\n" +
                "}  \t\t\t\t\n" +
                "} \n" +
                "}\n" +
                "}";

        Query q = QueryFactory.create(qs) ;

        try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(rs);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
