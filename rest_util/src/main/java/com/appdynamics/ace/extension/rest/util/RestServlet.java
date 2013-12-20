package com.appdynamics.ace.extension.rest.util;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.container.servlet.WebConfig;
import com.sun.jersey.spi.container.servlet.WebServletConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;



/**
 *
 * This should be done with a propper Application Class
 *
 *
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 21.10.13
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */


@WebServlet(urlPatterns="/restExtensions/*", initParams={
        @WebInitParam(name="jersey.config.server.provider.packages", value="com.appdynamics.ace.extension.rest.command,com.appdynamics.ace.extension.rest.util.command"),
        @WebInitParam(name= JSONConfiguration.FEATURE_POJO_MAPPING, value="true")},
        loadOnStartup = 1)

public class RestServlet extends ServletContainer {




}
