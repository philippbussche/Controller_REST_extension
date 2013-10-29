package com.appdynamics.ace.extension.rest.command;

import com.appdynamics.ace.extension.rest.util.BeanLocator;
import com.singularity.ee.controller.api.constants.AgentType;
import com.singularity.ee.controller.api.constants.EntityType;
import com.singularity.ee.controller.api.dto.Application;
import com.singularity.ee.controller.api.dto.EntityDefinition;
import com.singularity.ee.controller.api.dto.HierarchicalConfigKey;
import com.singularity.ee.controller.api.dto.transactionmonitor.transactiondefinition.EntryMatchPointConfig;
import com.singularity.ee.controller.api.dto.transactionmonitor.transactiondefinition.TransactionMatchPointConfig;
import com.singularity.ee.controller.api.exceptions.ServerException;
import com.singularity.ee.controller.api.services.model.IApplicationManager;
import com.singularity.ee.controller.api.services.model.IEntryMatchPointConfigManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 28.10.13
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
@Path("aceBTConfig")
public class BTConfigExportService {

    private IApplicationManager _applicationManager;
    private IEntryMatchPointConfigManager _configManager;

    public BTConfigExportService () {
        _configManager = BeanLocator.getInstance().getGlobalBeanInstance(IEntryMatchPointConfigManager.class);
        _applicationManager = BeanLocator.getInstance().getGlobalBeanInstance(IApplicationManager.class);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("export/{id}")
    public TransactionMatchPointConfig[] exportBTConfig(@PathParam("id") long id) throws ServerException {
        HierarchicalConfigKey configKey = new HierarchicalConfigKey();
        configKey.setAgentType(AgentType.APP_AGENT);

        configKey.setAttachedEntity(new EntityDefinition(EntityType.APPLICATION,id));

        TransactionMatchPointConfig[] configs = _configManager.getTransactionMatchPointConfigs(new HierarchicalConfigKey(new EntityDefinition(EntityType.APPLICATION, id), AgentType.APP_AGENT), false);


        return configs;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("apps")
    public Application[] exportApps(@PathParam("id") long id) throws ServerException {
        return _applicationManager.getAllApplications();

    }
}
