package com.bubobubo;


import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.http.HTTPTupleQuery;
import org.openrdf.rio.RDFFormat;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositoryFactory {

    private final static String SYSTEM = "SYSTEM";
    private final static Logger LOG = Logger.getLogger(RepositoryFactory.class);

    private String rootUri;
    private String repositoryName;
    private RepositoryConnection repositoryConnection;

    public RepositoryFactory(String rootUri) {
        setRootUri(rootUri);
    }

    public void createRepository() throws Exception {
        repositoryName = "test-repository-" + UUID.randomUUID();
        createRepository(repositoryName);
    }

    public void createRepository(String repositoryName) throws Exception {

        acquireConnection(SYSTEM);
        setRepositoryName(repositoryName);

        String contextName = "http://" + getRepositoryName();
        buildContext(contextName);
        buildRepository(contextName);
    }

    public void deleteRepository() throws Exception {
        deleteRepository(getRepositoryName());
    }

    public void deleteRepository(String repositoryName) throws Exception {
        setRepositoryName(repositoryName);
        String contextName = "http://" + getRepositoryName();
        removeContext(contextName);
        removeRepository(contextName);
    }

    public RepositoryConnection acquireConnection(String repositoryName) throws Exception {
        if(repositoryConnection == null) {
            repositoryConnection = new HTTPRepository(getRootUri() + repositoryName).getConnection();
        }
        return repositoryConnection;
    }

    public void closeConnection() throws Exception {
        if(repositoryConnection != null) {
            repositoryConnection.close();
        }
    }

    private boolean contextExists(String contextName) throws Exception {
        String query = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX sys:<http://www.openrdf.org/config/repository#>\n" +
                "ASK FROM <" + contextName + "> { ?s a sys:RepositoryContext . }";

        return executeBooleanQuery(query);
    }

    private boolean repositoryExists(String repositoryId) throws Exception {
        String query = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX sys:<http://www.openrdf.org/config/repository#>\n" +
                "ASK { ?c sys:repositoryID \"" + repositoryId + "\" . }";

        return executeBooleanQuery(query);
    }

    private void buildContext(String contextName) throws Exception {
        boolean contextExists = contextExists(contextName);
        if(!contextExists) {
            File n3File = new ClassPathResource("repository/dsp_test_node_context.n3").getFile();
            executeRdf(n3File, new URIImpl(contextName));
            LOG.info("Created context " + contextName);
        }
    }

    private void buildRepository(String contextName) throws Exception {
        boolean repositoryExists = repositoryExists(repositoryName);
        if(!repositoryExists) {
            File n3File = new ClassPathResource("repository/dsp_test_node_repo.n3").getFile();
            executeRdf(n3File, new URIImpl(contextName), repositoryName);
            LOG.info("Created repo " + repositoryName);
        }
    }

    private boolean executeBooleanQuery(String queryString) throws Exception {
        BooleanQuery booleanQuery = repositoryConnection.prepareBooleanQuery(QueryLanguage.SPARQL, queryString);
        return booleanQuery.evaluate();
    }

    private void executeRdf(File n3File, Resource context, String ... params) throws Exception {

        String fileAsString = FileUtils.readFileToString(n3File, "UTF-8");
        String formattedString = MessageFormat.format(fileAsString, params);

        repositoryConnection.add(new StringReader(formattedString), null, RDFFormat.N3, context);
    }

    private void removeContext(String contextName) throws Exception {
        boolean contextExists = contextExists(contextName);
        if(contextExists) {
            // delete it
            LOG.info("Deleting context " + contextName);
            List<Binding> nodes = getNodesForContext(contextName);
            for(Binding node : nodes) {
                String nodeValue = node.getValue().stringValue();
                LOG.info("Clearing node name: " + node.getValue().toString() + " (" + nodeValue + ")");

                Resource resource;
                if(nodeValue.startsWith("http")) {
                    resource = new URIImpl(node.getValue().toString());
                } else {
                    resource = new BNodeImpl(node.getValue().toString());
                }
                repositoryConnection.clear(resource);
            }
        }
    }

    private void removeRepository(String contextName) throws Exception {
        boolean repositoryExists = repositoryExists(repositoryName);
        if(repositoryExists) {
            LOG.info("Deleting repository");
            repositoryConnection.clear(new URIImpl(contextName));
            LOG.info("Deleted repository");
        }
    }

    private List<Binding> getNodesForContext(String contextName) throws Exception {
        String query = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX sys:<http://www.openrdf.org/config/repository#>\n" +
                "select DISTINCT ?s where {\n" +
                "graph <" + contextName + "> { ?s a sys:RepositoryContext . }}";

        TupleQueryResult result = executeHttpQuery(query);
        return getTupleItems(result, "s");
    }

    private TupleQueryResult executeHttpQuery(String queryString) throws Exception {
        HTTPTupleQuery query = (HTTPTupleQuery)repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        return query.evaluate();
    }

    private List<Binding> getTupleItems(TupleQueryResult result, String bindParameterName) throws Exception {
        List<Binding> tupleItems = new ArrayList<Binding>();
        while(result.hasNext()) {
            BindingSet tuple = result.next();
            Binding item = tuple.getBinding(bindParameterName);
            tupleItems.add(item);
        }
        return tupleItems;
    }

    public void setRootUri(String rootUri) {
        this.rootUri = rootUri;
    }

    public String getRootUri() {
        return rootUri;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }
}
