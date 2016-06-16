package com.github.angelndevil2.dsee.servlet;

import com.github.angelndevil2.dsee.server.ThreadLister;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @since 0.0.4
 * @author k, Created on 16. 2. 28.
 */
@Path("/thread")
public class ThreadList {

    private final ThreadLister lister = new ThreadLister();

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response list() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return Response.status(200).entity(gson.toJson(lister.getThreadList())).build();
    }
}
