package com.appdynamics.ace.extension.rest.util;

import com.appdynamics.ace.extension.rest.util.command.ExtensionInfo;
import com.appdynamics.ace.extension.rest.util.command.ExtensionInfoRest;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/**
 * Created by stefan.marx on 30.05.17.
 */
public class RestExtensionApplication extends ResourceConfig {

    private static final Logger LOGGER =
            Logger.getLogger(RestExtensionApplication.class.getName());

    public RestExtensionApplication () {
        LOGGER.info("Register Packages (json and serialization)");

        register(JacksonFeature.class);

        LOGGER.info("Register Extension Info !!!");

        register(ExtensionInfoRest.class);


    }
}
