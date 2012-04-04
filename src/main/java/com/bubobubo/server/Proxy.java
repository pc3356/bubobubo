package com.bubobubo.server;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ViewResource;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.spi.component.ProviderServices;
import com.sun.jersey.core.spi.factory.MessageBodyFactory;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

@Component
@Path("/connection")
public class Proxy {

    @GET
    public Response get(@Context Request request) throws Exception {

        Client c = Client.create();
        String uri = "http://static.bbci.co.uk/h4discoveryzone/ic/newsimg/media/images/229/129/59496000/jpg/_59496825_59496662.jpg";
        WebResource r = c.resource(uri);
        MediaType[] mediaTypes = {
                MediaType.APPLICATION_OCTET_STREAM_TYPE,
                MediaType.APPLICATION_JSON_TYPE,
                MediaType.APPLICATION_XHTML_XML_TYPE,
                MediaType.APPLICATION_XML_TYPE
        };
        r.accept(mediaTypes);
        ClientResponse response = r.get(ClientResponse.class);

        String entity = response.getEntity(String.class);

        return Response.status(response.getClientResponseStatus()).entity(entity).build();


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
