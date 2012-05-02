package com.bubobubo.server;

import com.bubobubo.service.SpringSecurityService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pjc
 * Date: 30/04/2012
 * Time: 23:00
 */
@Path("/")
public class SecuredProxy {

    private final static Logger LOGGER = Logger.getLogger(SecuredProxy.class);

    @Autowired
    private SpringSecurityService springSecurityService;

    //private String urlS = "http://localhost:9191/rest-endpoints/endpoints/endpoint";
    private String urlS = "http://localhost:9090/openrdf-sesame";
    private String repositoryId = "native-j-rdf";
    private String fullUrl = urlS + "/repositories/" + repositoryId;

    @Context
    Request request;

    @GET
    @Path("/sp/{uri: .*}")
    public Response get(@Context HttpHeaders headers, String uri) throws Exception {

        LOGGER.info("GET Path: " + uri);
        LOGGER.info("GET Going to " + fullUrl);

        Client c = Client.create();
        WebResource.Builder resourceBuilder = c.resource(fullUrl).getRequestBuilder();

        for(Map.Entry<String, List<String>> header:headers.getRequestHeaders().entrySet()){
            resourceBuilder.header(header.getKey(), header.getValue());
        }

        ClientResponse clientResponse = resourceBuilder.get(ClientResponse.class);

        Response.ResponseBuilder builder = Response
                .status(clientResponse.getStatus())
                .entity(clientResponse.getEntity(byte[].class))
                .type(clientResponse.getType());

        return builder.build();
    }

    @PUT
    @Path("/sp/{uri: .*}")
    public Response put(@Context HttpHeaders headers, String uri, BufferedReader reader) throws Exception {

        LOGGER.info("PUT Path: " + uri);
        LOGGER.info("PUT Going to " + fullUrl);

        Client c = Client.create();
        WebResource.Builder resourceBuilder = c.resource(urlS).entity(reader);

        for(Map.Entry<String, List<String>> header:headers.getRequestHeaders().entrySet()){
            resourceBuilder.header(header.getKey(), header.getValue());
        }

        ClientResponse clientResponse = resourceBuilder.put(ClientResponse.class, reader);

        Response.ResponseBuilder builder = Response
                .status(clientResponse.getStatus())
                .entity(clientResponse.getEntity(byte[].class))
                .type(clientResponse.getType());

        return builder.build();
    }

}
