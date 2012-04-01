package com.bubobubo.server;

import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Path("/connection")
public class Proxy {

    @GET
    public Response get() {
        return null;
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
