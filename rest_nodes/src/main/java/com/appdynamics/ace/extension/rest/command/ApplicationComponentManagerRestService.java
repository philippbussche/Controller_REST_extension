package com.appdynamics.ace.extension.rest.command;

import com.appdynamics.ace.extension.rest.command.api.RestException;
import com.appdynamics.ace.extension.rest.util.BeanLocator;
import com.singularity.ee.controller.api.exceptions.ServerException;
import com.singularity.ee.controller.api.services.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Created by philipp on 07/08/17.
 */
@Path("aceApplicationComponentService")
public class ApplicationComponentManagerRestService {

    private final IApplicationComponentManager _applicationComponentManager;

    public ApplicationComponentManagerRestService() {
        _applicationComponentManager = BeanLocator.getInstance().getGlobalBeanInstance(IApplicationComponentManager.class);
    }

    @PUT
    @Path("nodes/{nodeId}/moveTo/{newComponentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void moveNode(@PathParam("nodeId") String nodeId,@PathParam("newComponentId") String newComponentId) throws RestException {
        try {
            if(isNumeric(nodeId) && isNumeric(newComponentId)) {
                _applicationComponentManager.requestAgentReregistrationWithNewComponentAssignment(Long.parseLong(nodeId), Long.parseLong(newComponentId));
            } else {
                throw new RestException("Both nodeID and newComponentId have to be numeric values.");
            }
        }  catch (ServerException e) {
            throw new RestException("Error moving node with ID "+nodeId+ " to tier with ID " + newComponentId + ":" + e.getMessage());
        }
    }


    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

}