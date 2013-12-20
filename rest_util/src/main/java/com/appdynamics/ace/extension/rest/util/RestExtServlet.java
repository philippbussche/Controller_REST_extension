/*
 * Copyright (c) AppDynamics Inc
 * All rights reserved
 */

package com.appdynamics.ace.extension.rest.util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.container.servlet.WebConfig;

/**
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 29.11.13
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */

@WebServlet(urlPatterns="/extensionApi/*", initParams={
        @WebInitParam(name= JSONConfiguration.FEATURE_POJO_MAPPING, value="true")},
        loadOnStartup = 1)
public class RestExtServlet extends ServletContainer
{
    private static RestExtServlet _instance;
    private RestExtensionsResource _ctx;

    public RestExtServlet() {
        super();
        _instance = this;
    }



    private static final String CONTROLLER_HOME_PROPERTY_KEY = "appdynamics.controller.home";

    public static File getRestExtensionsRootDir()
    {
        File customDir = new File(System.getProperty(CONTROLLER_HOME_PROPERTY_KEY),"custom");
        return new File(customDir,"restExtensions");
    }


    @Override
    protected ResourceConfig getDefaultResourceConfig(Map<String, Object> props, WebConfig wc) throws ServletException
    {

        System.out.println("XXXXXXXXXXXX : Started ");
        File jarPath = getRestExtensionsRootDir();

          //      new File("./build/extensions/webservices");

        try {

            if (!jarPath.exists())  jarPath.mkdirs();
        }   catch (Exception ex ) {
            ex.printStackTrace();
            throw new ServletException("Cannot create Extensions Path/Ext Path "+jarPath.getAbsolutePath()+") doesn't exist)",ex);
        }

        if (!jarPath.canRead()) throw new ServletException("Cannot read in Ext Dir ("+jarPath.getAbsolutePath()+")");

          _ctx = new RestExtensionsResource(jarPath);

      return _ctx;
    }

    public static RestExtServlet getInstance() {
        return _instance;
    }

    public void reloadContext() {
        this.reload();
    }
}
