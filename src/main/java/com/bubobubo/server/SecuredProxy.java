package com.bubobubo.server;

import com.bubobubo.service.SpringSecurityService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pjc
 * Date: 30/04/2012
 * Time: 23:00
 */
public class SecuredProxy {

    @Autowired
    private SpringSecurityService springSecurityService;

    private String urlS = "http://localhost:9191/rest-endpoints/endpoints/endpoint";

    @GET
    @Path("/sp/{uri: .*}")
    public Response proxy(@Context HttpHeaders headers, String requestBody) throws Exception {

        Client c = Client.create();
        WebResource.Builder resourceBuilder = c.resource(urlS).entity(requestBody);

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

}
