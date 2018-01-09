package com.FrontLayer;

import org.apache.jena.query.*;

/**
 * Created by marne on 2/25/2017.
 */
public class Crud {

    public void queryDataPrint(String serviceURI){
        String query = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                " Select ?subject ?predicate ?object ?subject2 " +
                "where {" +
                " { ?subject rdfs:label ?result .  FILTER regex(str(?result),\".*English*.\")} " +
                " union " +
                " { ?subject rdfs:comment ?result1 .  FILTER regex(str(?result1),\".*English*.\")} . " +
                " {?subject ?predicate ?object} union {?subject2 ?predicate ?subject} . " +
                " } ";
               // " limit 2 ";


        String query2 ="PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "SELECT ?subject ?predicate ?object " +
                "        WHERE {?subject ?predicate ?object} " +
                "        LIMIT 200";


        Query queryObj = QueryFactory.create(query2);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService(serviceURI,queryObj);

        ResultSet resultSet = queryExecution.execSelect();
        ResultSetFormatter.out(System.out,resultSet);

        queryExecution.close();

    }

    public static void  main(String[] args){
        Crud crud = new Crud();
        //crud.queryDataPrint("http://localhost:3030/BritishLibraryNew/query");
        crud.queryDataPrint("http://localhost:3030/HealthDataSet/query");
    }
}
