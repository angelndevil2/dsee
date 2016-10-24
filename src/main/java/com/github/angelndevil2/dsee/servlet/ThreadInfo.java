package com.github.angelndevil2.dsee.servlet;

import com.github.angelndevil2.dsee.server.thread.ThreadManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.*;
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
    public Response list(@QueryParam("callback") String cb) {

        Gson gson = new GsonBuilder().serializeNulls().create();
        String ret = gson.toJson(manager.getThreadList());
        if (cb != null) ret = cb + "(" + ret + ")";
        return Response.status(200).entity(ret).build();
    }

    @GET
    @Path("dump/{id}")
    @Produces(MediaType.TEXT_PLAIN+ ";charset=utf-8")
    public Response dump(@QueryParam("callback") String cb, @PathParam("id") long id) {

        Gson gson = new GsonBuilder().serializeNulls().create();
        return Response.status(200).entity(gson.toJson(manager.toString(id))).build();
    }

    /**
     *
     * @param id thread id
     * @return ThreadInfo's JSONString representation
     */
    @GET
    @Path("info/{id}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response info(@PathParam("id") long id, @QueryParam("callback") String cb) {

        Gson gson = new GsonBuilder().serializeNulls().create();
        String ret = gson.toJson(manager.toJSONString(id));
        if (cb != null) ret = cb + "(" + ret + ")";

        return Response.status(200).entity(ret).build();
    }
}
