package com.bubobubo;


import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ConnectionTest extends EmbeddedJettyServer {


    @BeforeClass
    public static void setupClass() throws Exception {
        // start the jetty server
        useWebXmlFileFromClasspath("META-INF/web.xml");
        start();
        // create the in mem repo

    }

    @Test
    public void getConnectionToInMemRepoViaProxy(){
        // try and make successful conn
        fail("make this work");
    }
}
