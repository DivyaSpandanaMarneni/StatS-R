package com.extension;

import com.rIntegration.StandardDeviation;
import org.apache.jena.atlas.io.IndentedLineBuffer;
import org.apache.jena.graph.Node;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.expr.*;
import org.apache.jena.sparql.expr.aggregate.*;
import org.apache.jena.sparql.function.FunctionEnv;
import org.apache.jena.sparql.graph.NodeConst;
import org.apache.jena.sparql.serializer.SerializationContext;
import org.apache.jena.sparql.sse.writers.WriterExpr;
import org.apache.jena.sparql.util.ExprUtils;
import org.apache.jena.sparql.util.FmtUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Divya on 4/29/2017.
 */
public class AggCustomCheck extends AggCustom {
    private final String iri;
    public static ArrayList<Double> arrayList = new ArrayList<>();

    public AggCustomCheck(String iri, boolean distinct, ExprList exprs) {
        super("SD", distinct, exprs);
        this.iri = iri;
    }

    @Override
    public Aggregator copy(ExprList exprs) {
        return new AggCustomCheck(iri, isDistinct, exprs);
    }

    @Override
    public String asSparqlExpr(SerializationContext sCxt) {
        IndentedLineBuffer x = new IndentedLineBuffer();
        if ( !AggregateRegistry.isRegistered(iri) ) {

            x.append(getName());
            x.append(" ");
        }

        String uriStr = FmtUtils.stringForURI(iri, sCxt) ;
        x.append(uriStr) ;
        x.append("(");
        if ( isDistinct )
            x.append("DISTINCT ");
        x.incIndent();
        ExprUtils.fmtSPARQL(x, getExprList(), sCxt);
        x.decIndent();
        x.append(")");
        return x.asString();
    }

    @Override
    public String toPrefixString() {
        IndentedLineBuffer x = new IndentedLineBuffer();
        x.append("(");
        x.append(getName().toLowerCase(Locale.ROOT));
        x.append(" <");
        x.append(iri);
        x.append("> ");
        x.incIndent();

        if ( isDistinct )
            x.append("distinct ");
        boolean first = true;
        for ( Expr e : getExprList() ) {
            if ( !first )
                x.append(" ");
            first = false;
            WriterExpr.output(x, e, null);
            first = false;
        }
        x.decIndent();
        x.append(")");
        return x.asString();
    }

    @Override
    public Accumulator createAccumulator() {
        AccumulatorFactory f = AggregateRegistry.getAccumulatorFactory(iri);
        if ( f == null )
            throw new QueryExecException("Unregistered aggregate: " + iri);
        return f.createAccumulator(this, isDistinct);
    }

    public static AccumulatorFactory accFactory = new AccumulatorFactory() {
        @Override
        public Accumulator createAccumulator(AggCustom agg, boolean distinct) {
            return new AccCustom(agg);
        }
    };

    @Override
    public Node getValueEmpty() {
        return AggregateRegistry.getNoGroupValue(iri);
    }

    @Override
    public Expr getExpr() {
        if ( exprList.size() == 0 )
            return null;
        else
            return exprList.get(0);
    }

    public String getIRI() {
        return iri;
    }

    @Override
    public int hashCode() {
        if ( !AggregateRegistry.isRegistered(iri) ) {
            return asFunction().hashCode();
        }
        return HC_AggCustom ^ getExprList().hashCode() ^ iri.hashCode();
    }

    private E_Function asFunction() {
        return new E_Function(iri, exprList);
    }

    @Override
    public boolean equals(Aggregator other, boolean bySyntax) {
        if ( other == null )
            return false;
        if ( this == other )
            return true;

        if ( !AggregateRegistry.isRegistered(iri) ) {
            E_Function f1 = asFunction();
            if ( !(other instanceof AggCustomCheck) )
                return false;
            E_Function f2 = ((AggCustomCheck)other).asFunction();
            return f1.equals(f2, bySyntax);
        }

        if ( !(other instanceof AggCustomCheck) )
            return false;
        AggCustomCheck agg = (AggCustomCheck)other;
        return Objects.equals(this.iri, agg.iri) && this.isDistinct == agg.isDistinct
                && this.getExprList().equals(agg.getExprList(), bySyntax);
    }

    public static Accumulator createAccNull() {
        return new AccCustom();
    }

    // ---- Accumulator
    private static class AccCustom implements Accumulator {
        private int nBindings = 0;
        double sd = 0;
        private AggCustom aggCustom;

        public AccCustom() {}

        public AccCustom(AggCustom aggCustom){
            this.aggCustom = aggCustom;
        }

        @Override
        public void accumulate(Binding binding, FunctionEnv functionEnv) {
            ExprList exprList = aggCustom.getExprList();

            for (Expr expr :
                    exprList) {

                try {
                    NodeValue nv = expr.eval(binding, functionEnv);
                    // Evaluation succeeded.
                    if ( nv.isLiteral()) {
                        arrayList.add(Double.parseDouble(nv.asUnquotedString()));
                    }
                } catch (ExprEvalException ex) {
                }

            }


            //System.out.println("Standard deviation is "+sd);
        }

        @Override
        public NodeValue getValue() {
            System.out.println("Hitting getValue");
            StandardDeviation standardDeviation = new StandardDeviation();

            sd = standardDeviation.getSD(arrayList);
            System.out.println("sd here "+sd);
            return NodeValue.makeDouble(sd);
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        CustomAggregateSD customAggregateSD = new CustomAggregateSD();

        String aggUri = "http://example/getSD" ;
        AggregateRegistry.register(aggUri,accFactory, NodeConst.nodeMinusOne);
        //AggregateRegistry.register(aggUri,, NodeConst.nodeMinusOne);

        //String qs = "SELECT (<http://example/getSD>(?o) AS ?sd) {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;
        String qs = "SELECT (<http://example/getSD>(?o) AS ?agg) where {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o}" ;

        String qsTest = "SELECT ?s ?p ?o where {?s <http://chronicdata.cdc.gov/resource/g4ie-h725/highconfidencelimit> ?o } ";


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
