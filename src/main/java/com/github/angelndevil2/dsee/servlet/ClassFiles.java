package com.github.angelndevil2.dsee.servlet;

import com.github.angelndevil2.dsee.context.GlobalContext;
import com.github.angelndevil2.dsee.server.clazz.ClassFileManager;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * response path of classes related with jvm path properties
 *
 * @since 1.4.0
 *
 * Created by k on 16. 10. 17.
 */
@Slf4j
@Path("/class-files")
public class ClassFiles {

    private static final ClassFileManager classFileManager = GlobalContext.getInstance().getClassFileManager();

    /**
     *
     * @return Json string for class file names in class path <br/>
     * Map&lt;jar_file_or_path_name, class_file_name_array&gt;
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response all() {

        return Response.status(200).entity(classFileManager.toJSONString()).build();
    }

    /**
     *
     * @return Json string for class file names in class path <br/>
     * Map&lt;jar_file_or_path_name, class_file_name_array&gt;
     */
    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response classes() {

        return Response.status(200).entity(classFileManager.getUserClasses().toJSONString()).build();
    }

    /**
     *
     * @return Json string for class file names in boot class path <br/>
     * Map&lt;jar_file_or_path_name, class_file_name_array&gt;
     */
    @GET
    @Path("boot")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response boot() {
        return Response.status(200).entity(classFileManager.getBootClasses().toJSONString()).build();
    }

    /**
     *
     * @return Json string for class file names in endorsed class path <br/>
     * Map&lt;jar_file_or_path_name, class_file_name_array&gt;
     */
    @GET
    @Path("endorsed")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response endorsed() {
        return Response.status(200).entity(classFileManager.getEndorsedClasses().toJSONString()).build();
    }

    /**
     *
     * @return Json string for class file names in ext class path <br/>
     * Map&lt;jar_file_or_path_name, class_file_name_array&gt;
     */
    @GET
    @Path("ext")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response ext() {
        return Response.status(200).entity(classFileManager.getExtClasses().toJSONString()).build();
    }
}
