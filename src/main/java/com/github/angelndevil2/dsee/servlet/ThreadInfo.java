package com.github.angelndevil2.dsee.servlet;

import com.github.angelndevil2.dsee.server.thread.ThreadManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @since 0.0.4
 * @author k, Created on 16. 2. 28.
 */
@Path("/thread")
public class ThreadInfo {

    private final ThreadManager manager = new ThreadManager();

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response list() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return Response.status(200).entity(gson.toJson(manager.getThreadList())).build();
    }

    @GET
    @Path("dump/{id}")
    @Produces(MediaType.TEXT_PLAIN+ ";charset=utf-8")
    public Response dump(@PathParam("id") long id) {
        return Response.status(200).entity(manager.dump(id)).build();
    }
}
