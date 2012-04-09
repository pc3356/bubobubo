package com.bubobubo.server;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Component
@Path("/connection")
public class Proxy {

    private final static Logger LOGGER = Logger.getLogger(Proxy.class);

    @GET
    public Response get(@Context Request request) throws Exception {

        Client c = Client.create();
        String uri = "http://static.bbci.co.uk/h4discoveryzone/ic/newsimg/media/images/229/129/59496000/jpg/_59496825_59496662.jpg";
        WebResource r = c.resource(uri);
        ClientResponse response = r.get(ClientResponse.class);
        byte[] entity = response.getEntity(byte[].class);
        return Response.status(response.getClientResponseStatus()).entity(entity).type(response.getType()).build();

    }

    @PUT
    public Response put() {
        return null;
    }

    @POST
    public Response post() {
        return null;
    }

    @DELETE
    public Response delete() {
        return null;
    }

    @OPTIONS
    public Response options() {
        return null;
    }

}
