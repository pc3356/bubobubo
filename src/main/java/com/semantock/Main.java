package com.semantock;

import org.openrdf.model.Resource;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.http.HTTPRepository;

public class Main {

    /*
        Noddy main method to show connecting to an existing HTTPRepository - in this case not so useful, as it's tied
        to being a BBC one...
     */
    public static void main(String[] args) throws Exception {

        HTTPRepository repository = new HTTPRepository("http://pal.sandbox.dev.bbc.co.uk:8173/bigowlim-sesame", "dsp-cluster");
        RepositoryConnection repositoryConnection = repository.getConnection();

        RepositoryResult<Resource> contexts = repositoryConnection.getContextIDs();
        while(contexts.hasNext()) {
            URIImpl context = (URIImpl)contexts.next();
            System.out.println(context.getNamespace() + context.getLocalName());
        }

    }
    
}
