package com.github.angelndevil2.dsee.servlet;

import com.github.angelndevil2.dsee.iface.IClassSearch;
import com.github.angelndevil2.dsee.search.ClassSearch;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONAware;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author k on 16. 10. 18.
 */
@Slf4j
@Path("/search/class")
public class SearchClass {

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response classes(@PathParam("name") String name) {

        JSONAware ret = classSearch.findName(name);

        return Response.status(Response.Status.OK).entity(ret.toJSONString()).build();
    }

    private IClassSearch classSearch = new ClassSearch();
}
