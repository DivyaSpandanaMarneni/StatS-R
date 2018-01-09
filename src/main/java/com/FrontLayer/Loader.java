package com.FrontLayer;

import org.apache.jena.base.Sys;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.*;

/**
 * Created by Divya on 4/2/2017.
 */
public class Loader {

    public void upLoader(String dataAccessorURI) throws FileNotFoundException{
        //DatasetAccessorFactory datasetAccessorFactory;
        DatasetAccessor accessor;
        accessor = DatasetAccessorFactory.createHTTP(dataAccessorURI);
        //step2 - creation of datamodel from inputstream
        File rdfFile = new File("V:/MS/Thesis/Thesis Work Experiments/BNBLODBooks_sample_rdf/BNBLODB_sample.rdf");
        FileInputStream fileInputStream = new FileInputStream(rdfFile);

        Model model = ModelFactory.createDefaultModel();
        model.read(fileInputStream,null,"RDF/XML");

        accessor.putModel(model);
    }

    public static void main(String[] args) throws IOException, FileNotFoundException{
        System.out.println("Enter the URI...");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String uri = bufferedReader.readLine();

        Loader loader = new Loader();
        loader.upLoader(uri.trim());
    }

}
