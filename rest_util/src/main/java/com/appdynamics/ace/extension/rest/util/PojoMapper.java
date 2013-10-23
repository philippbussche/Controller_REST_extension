package com.appdynamics.ace.extension.rest.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import javax.ws.rs.ext.ContextResolver;

/**
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 21.10.13
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class PojoMapper implements ContextResolver<ObjectMapper> {


        final ObjectMapper defaultObjectMapper;


        public PojoMapper() {
            defaultObjectMapper = createDefaultMapper();
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
                return defaultObjectMapper;
        }

        private static ObjectMapper createDefaultMapper() {
            final ObjectMapper result = new ObjectMapper();
            result.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

            return result;
        }
}
