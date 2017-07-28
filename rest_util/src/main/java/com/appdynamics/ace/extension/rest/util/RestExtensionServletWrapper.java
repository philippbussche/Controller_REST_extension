package com.appdynamics.ace.extension.rest.util;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.WebConfig;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import java.util.logging.Logger;

/**
 * Created by stefan.marx on 30.05.17.
 */

@WebServlet(urlPatterns="/extensionApi/*", initParams={
        @WebInitParam(name = "jersey.config.server.provider.classnames",value = "org.glassfish.jersey.media.multipart.MultiPartFeature")},
        loadOnStartup = 1)
public class RestExtensionServletWrapper extends ServletContainer {

    private static final Logger LOGGER =
            Logger.getLogger(RestExtensionServletWrapper.class.getName());

    @Override
    protected void init(WebConfig webConfig) throws ServletException {
        super.init(webConfig);
        LOGGER.info("RestWrapper initialized !!!");
        reload(new RestExtensionApplication());
    }
}
