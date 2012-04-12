package com.bubobubo.server;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Component
@Path("/")
public class Proxy {

    private final static Logger LOGGER = Logger.getLogger(Proxy.class);

    private String targetUri = "http://localhost:9090";

    @GET
    @Path("/{uri: .*}")
    public Response getUri(@Context HttpHeaders headers, @PathParam("uri") String uri, String requestBody) throws Exception {

        String target = buildUrl(headers, uri);
        LOGGER.info("Fetching uri: " + target);

        Client c = Client.create();
        WebResource.Builder resourceBuilder = c.resource(target).entity(requestBody);

        ClientResponse clientResponse = resourceBuilder.get(ClientResponse.class);

        for(Map.Entry<String, List<String>> header : headers.getRequestHeaders().entrySet()) {
            resourceBuilder.header(header.getKey(), header.getValue());
        }

        Response.ResponseBuilder builder = Response
                .status(clientResponse.getStatus())
                .entity(clientResponse.getEntity(byte[].class))
                .type(clientResponse.getType());

        return builder.build();

    }

    @GET
    public Response get(@Context HttpHeaders headers, String requestBody) throws Exception {
        return buildResponse(
                buildResource(headers, requestBody).get(ClientResponse.class)
        );
    }

    private WebResource.Builder buildResource(HttpHeaders headers, String requestBody) {

        Client c = Client.create();
        WebResource.Builder resourceBuilder = c.resource(targetUri).entity(requestBody);
        
        for(Map.Entry<String, List<String>> header:headers.getRequestHeaders().entrySet()){
            resourceBuilder.header(header.getKey(), header.getValue());
        }
        
        return resourceBuilder;
    }

    @PUT
    public Response put(@Context HttpHeaders headers, String requestBody) throws Exception {
        return buildResponse(
                buildResource(headers, requestBody).put(ClientResponse.class)
        );
    }

    @POST
    public Response post(@Context HttpHeaders headers, String requestBody) throws Exception {
        return buildResponse(
                buildResource(headers, requestBody).post(ClientResponse.class)
        );
    }

    @DELETE
    public Response delete(@Context HttpHeaders headers, String requestBody) throws Exception {
        return buildResponse(
                buildResource(headers, requestBody).delete(ClientResponse.class)
        );
    }

    @OPTIONS
    public Response options(@Context HttpHeaders headers, String requestBody) throws Exception {
        return buildResponse(
                buildResource(headers, requestBody).options(ClientResponse.class)
        );
    }

    @HEAD
    public Response head(@Context HttpHeaders headers, String requestBody) throws Exception {
        return buildResponse(
                buildResource(headers, requestBody).head()
        );
    }


    public Response buildResponse(ClientResponse clientResponse) throws Exception {
        Response.ResponseBuilder builder = Response
                .status(clientResponse.getStatus())
                .entity(clientResponse.getEntity(byte[].class))
                .type(clientResponse.getType());

        return builder.build();

    }

    private String buildUrl(HttpHeaders headers, String uri) throws Exception {

        StringBuilder builder = new StringBuilder(targetUri);
        if(headers.getRequestHeader("referer") != null) {
            String referrer = headers.getRequestHeader("referer").get(0);

            try {
                URL url = new URL(referrer);
                builder.append(url.getPath());
            } catch (MalformedURLException mue) {
                /* don't do much.... */
                throw new Exception(mue);
            }
        }
        return builder.append('/').append(uri).toString();
    }

}
