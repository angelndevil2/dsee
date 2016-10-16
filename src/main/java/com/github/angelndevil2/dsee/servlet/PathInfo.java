package com.github.angelndevil2.dsee.servlet;

import com.github.angelndevil2.dsee.util.JVMUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.github.angelndevil2.dsee.util.JVMUtil.*;

/**
 * response path of classes related with jvm path properties
 *
 * @since 1.4.0
 *
 * Created by k on 16. 10. 17.
 */
@Path("/path")
public class PathInfo {

    @GET
    @Path("class")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response classes() {

        Gson gson = new GsonBuilder().serializeNulls().create();
        return Response.status(200).entity(gson.toJson(JVMUtil.getClassPath())).build();
    }

    @GET
    @Path("boot")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response boot() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return Response.status(200).entity(gson.toJson(JVMUtil.getBootClassPath())).build();
    }

    @GET
    @Path("endorsed")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response endorsed() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return Response.status(200).entity(gson.toJson(JVMUtil.getEndorsedPaths())).build();
    }

    @GET
    @Path("ext")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response ext() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return Response.status(200).entity(gson.toJson(JVMUtil.getExtPaths())).build();
    }
}
