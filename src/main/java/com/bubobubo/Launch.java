package com.bubobubo;

import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: pjc
 * Date: 30/04/2012
 * Time: 18:20
 */
public class Launch {

    private final static String fullUrl = "http://localhost:8080/sp/something";
    private final static String realUrl = "http://localhost:9090/openrdf-sesame";
    private final static String proxyRepoId = "repositoryId";
    private final static String repositoryId = "native-j-rdf";

    public static void main(String[] args) throws Exception {

        HTTPRepository repository = new HTTPRepository(fullUrl, proxyRepoId);
        //HTTPRepository repository = new HTTPRepository(realUrl, repositoryId);

        repository.initialize();
        repository.setUsernameAndPassword("user", "password");

        RepositoryConnection connection = repository.getConnection();


        URI subject = new URIImpl("http://localhost/things/" + System.currentTimeMillis());
        URI predicate = new URIImpl("http://localhost/onto#knows");
        Value object = new LiteralImpl(UUID.randomUUID().toString());
        URI context = new URIImpl("http://localhost/context/12345");

        connection.add(new StatementImpl(subject, predicate, object), context);

        connection.close();
    }

}
