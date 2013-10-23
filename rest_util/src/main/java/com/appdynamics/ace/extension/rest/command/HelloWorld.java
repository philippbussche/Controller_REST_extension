package com.appdynamics.ace.extension.rest.command;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 21.10.13
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
@Path("hw")
public class HelloWorld {

    @GET
    @Path ("hw1")
    @Produces (MediaType.APPLICATION_JSON)
    public String world1 () {
        return "WORLD";
    }


    @GET
    @Path ("hw2")
    @Produces (MediaType.APPLICATION_JSON)
    public HelloWorldData world2 () {
        HelloWorldData data = new HelloWorldData();
        data.setMsg("WORLD 2!");
        return data;
    }
    @GET
    @Path ("hw3")
    @Produces (MediaType.APPLICATION_JSON)
    public HelloWorldData world3 () throws Exception {
        HelloWorldData data = new HelloWorldData();
        data.setMsg("WORLD 2!");
        if (2+3 > 3) {
            WebApplicationException ex = new WebApplicationException(Response.Status.BAD_REQUEST);
            throw new NotAuthorizedException("Main Failure");
        }
        return data;
    }

    public static class NotAuthorizedException extends WebApplicationException {
        public NotAuthorizedException(String message) {
            super(Response.status(Response.Status.BAD_REQUEST)
                    .entity(message).type(MediaType.TEXT_PLAIN).build());
        }
    }


}
