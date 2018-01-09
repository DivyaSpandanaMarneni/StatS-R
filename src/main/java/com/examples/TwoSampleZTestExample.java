package com.examples;

import com.extension.TwoSampleZTestUDF;
import org.apache.jena.graph.Node;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.nodevalue.NodeValueString;
import org.apache.jena.sparql.resultset.ResultsFormat;

import java.io.*;
import java.util.List;

/**
 * Created by Divya on 12/16/2017.
 */
public class TwoSampleZTestExample {
    public static void main(String[] args) throws FileNotFoundException{
        TwoSampleZTestUDF twoSampleZTestUDF = new TwoSampleZTestUDF();

        //String qs = "PREFIX fn:<http://example.org/function#> SELECT (fn:twoZTest(group_concat(?o;separator=','),group_concat(?o;separator=','),145.67908,145.67908,50) AS ?zstatistic) {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;

        String qs = "PREFIX fn:<http://example.org/function#> \n" +
                "Select ?oak\n" +
                "where{\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/locationabbr> \"AL\" .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/year> \"2013-2014\"  .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/topicdesc> \"Cigarette Use (Adults)\" .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/measuredesc> \"Current Smoking – (2 yrs – Race/Ethnicity)\" .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/data_value> ?oak .\t\t\n" +
                "filter(regex(?oak ,'[0-9.]')) \t\n" +
                "}";
        Model model = ModelFactory.createDefaultModel();
        String fileLocation="V:\\MS\\Thesis\\Thesis Work Experiments\\Datasets\\TobaccoUse.rdf";
        StringBuilder sbPopulaltion1 = new StringBuilder();
        StringBuilder sbPopulaltion2 = new StringBuilder();
        FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
        model.read(fileInputStream,null);
        Dataset dataset = DatasetFactory.create(model);
        Query q = QueryFactory.create(qs) ;
        try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
            ResultSet rs = qexec.execSelect() ;

            while (rs.hasNext()){
                sbPopulaltion2.append(rs.next().getLiteral("?oak"));
                sbPopulaltion2.append(",");
            }
            sbPopulaltion2.deleteCharAt(sbPopulaltion2.length()-1);
            //System.out.println(sbPopulaltion1);
        }catch (Exception e){
            e.printStackTrace();
        }

        qs = "PREFIX fn:<http://example.org/function#> \n" +
                "Select ?oga\n" +
                "where{\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/locationabbr> \"GA\" .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/year> \"2013-2014\"  .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/topicdesc> \"Cigarette Use (Adults)\" .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/measuredesc> \"Current Smoking – (2 yrs – Race/Ethnicity)\" .\n" +
                "\t?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/data_value> ?oga .\t\t\n" +
                "filter(regex(?oga ,'[0-9.]')) \t\n" +
                "}";
        q = QueryFactory.create(qs);

        try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
            ResultSet rs = qexec.execSelect() ;

            while (rs.hasNext()){
                sbPopulaltion1.append(rs.next().getLiteral("?oga"));
                sbPopulaltion1.append(",");
            }
            sbPopulaltion1.deleteCharAt(sbPopulaltion1.length()-1);

            //System.out.println(sbPopulaltion1);
        }catch (Exception e){
            e.printStackTrace();
        }

        NodeValue n1 = new NodeValueString(sbPopulaltion1.toString());
        NodeValue n2 = new NodeValueString(sbPopulaltion2.toString());


//        qs = "PREFIX fn:<http://example.org/function#> \n" +
//                "SELECT (fn:twoZTest("+n2+","+n1+",64.78244,65.13727,35) AS ?zstatistic) {}";
        qs = "PREFIX fn:<http://example.org/function#> \n" +
                "Select (fn:twoZTest(?oakConcat,?ogaConcat,65.13727,64.78244,35) as ?zstat)\n" +
                "where{\n" +
                "  {\n" +
                "\t\t\t\tSelect (group_concat(?oak;separator=':') as ?oakConcat) \n" +
                "                where{ \n" +
                "                ?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/locationabbr> \"AK\" . \n" +
                "                ?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/year> \"2013-2014\"  . \n" +
                "                ?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/topicdesc> \"Cigarette Use (Adults)\" . \n" +
                "                ?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/measuredesc> \"Current Smoking – (2 yrs – Race/Ethnicity)\" . \n" +
                "                ?s <https://chronicdata.cdc.gov/resource/wsas-xwh5/data_value> ?oak .\n" +
                "                filter(regex(?oak ,'[0-9.]')) .\n" +
                "                } \n" +
                "  }\n" +
                "  {\n" +
                "\n" +
                "\t\t\t\tSelect (group_concat(?oga;separator=':') as ?ogaConcat)\n" +
                "\t\t\t\twhere{\n" +
                "\t\t\t\t?s1 <https://chronicdata.cdc.gov/resource/wsas-xwh5/locationabbr> \"GA\" . \n" +
                "                ?s1 <https://chronicdata.cdc.gov/resource/wsas-xwh5/year> \"2013-2014\"  . \n" +
                "                ?s1 <https://chronicdata.cdc.gov/resource/wsas-xwh5/topicdesc> \"Cigarette Use (Adults)\" . \n" +
                "                ?s1 <https://chronicdata.cdc.gov/resource/wsas-xwh5/measuredesc> \"Current Smoking – (2 yrs – Race/Ethnicity)\" . \n" +
                "                ?s1 <https://chronicdata.cdc.gov/resource/wsas-xwh5/data_value> ?oga .\n" +
                "                filter(regex(?oga ,'[0-9.]')) \n" +
                "\t\t\t\t}\n" +
                "  }\n" +
                "\n" +
                "}";
        q = QueryFactory.create(qs);
        try ( QueryExecution qexec = QueryExecutionFactory.create(q, dataset) ) {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(rs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
