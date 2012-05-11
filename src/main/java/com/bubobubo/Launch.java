package com.bubobubo;

import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.resultio.TupleQueryResultFormat;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPGraphQuery;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.http.HTTPTupleQuery;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: pjc
 * Date: 30/04/2012
 * Time: 18:20
 */
public class Launch {

    private final static String proxyUrl = "http://localhost:8080/bubobubo/";
    private final static String actualRepositoryUrl = "http://localhost:9090/openrdf-sesame";
    private final static String testUrl = "http://localhost:9191/rest-endpoints";
    private final static String repositoryId = "native-j-rdf";

    public static void main(String[] args) throws Exception {

        HTTPRepository repository = new HTTPRepository(testUrl, repositoryId);

        repository.initialize();
        // authorization :: Basic dXNlcjpwYXNzd29yZA==
        repository.setUsernameAndPassword("user", "password");

        RepositoryConnection connection = repository.getConnection();

        try {
//  CONSTRUCT
//            HTTPGraphQuery graphQuery =
//                    (HTTPGraphQuery)connection.prepareGraphQuery(
//                            org.openrdf.query.QueryLanguage.SPARQL, "SELECT ?s WHERE {?s ?p ?o}");
//            StringWriter out = new StringWriter();
//            RDFWriter w = Rio.createWriter(RDFFormat.N3, out);
//            graphQuery.evaluate(w);
            //return stringout.toString();

            // POST
            HTTPTupleQuery tupleQuery = (HTTPTupleQuery)connection.prepareTupleQuery(
                    QueryLanguage.SPARQL,
                    "SELECT ?s WHERE {?s ?p ?o}");
            repository.setPreferredRDFFormat(RDFFormat.N3);
            repository.setPreferredTupleQueryResultFormat(TupleQueryResultFormat.CSV);
            TupleQueryResult result = tupleQuery.evaluate();

            // PUT
//            URI subject = new URIImpl("http://localhost/things/" + System.currentTimeMillis());
//            URI predicate = new URIImpl("http://localhost/onto#knows");
//            Value object = new LiteralImpl(UUID.randomUUID().toString());
//            URI context = new URIImpl("http://localhost/context/12345");
//
//            connection.add(new StatementImpl(subject, predicate, object), context);

        } finally {
            connection.close();
        }

    }

}
