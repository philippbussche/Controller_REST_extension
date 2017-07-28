package com.appdynamics.ace.extension.rest.util;

import com.appdynamics.ace.extension.rest.util.command.ExtensionInfoRest;
import org.glassfish.jersey.internal.util.ReflectionHelper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ResourceFinder;
import org.glassfish.jersey.server.internal.scanning.FilesScanner;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.WebConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by stefan.marx on 30.05.17.
 */

@WebServlet(urlPatterns="/extensionApi/*", 
//        initParams={
//        @WebInitParam(name = "jersey.config.server.provider.classnames",value = "org.glassfish.jersey.media.multipart.MultiPartFeature")},
        loadOnStartup = 1)
public class RestExtensionServletWrapper extends ServletContainer {


    private static final Logger LOG =
            Logger.getLogger(RestExtensionServletWrapper.class.getName());

    public static RestExtensionServletWrapper getInstance() {
        return _instance;
    }

    private static RestExtensionServletWrapper _instance;

    @Override
    protected void init(WebConfig webConfig) throws ServletException {
        super.init(webConfig);
        LOG.info("INIT");


        File extDir = getRestExtensionsRootDir();
        if (!extDir.exists()) {
            try {
                extDir.mkdirs();
            } catch (Throwable t) {
                LOG.log(Level.SEVERE,"Error while bootstraping ext dir at :"+extDir.getAbsolutePath(),t);
            }
        }

        ResourceConfig cfg = createConfig();
        reload(cfg);

        // start the thread
        new Thread(new CfgUpdater(getExtensionContentHash())).start();

        _instance = this;

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("FoundAuth: " + request.getUserPrincipal());
        LOG.info( "Header:"+request.getHeader("Authorization"));
        super.service(request, response);
    }

    private void updateResourceCfg() {
        // Later this might be a bit more sophisticated
        ResourceConfig cfg = createConfig();

        StringBuffer b = new StringBuffer("Updated Config Classes :\n");
        for (Class clazz:cfg.getClasses()) {
            b.append(clazz.getName())
                    .append("\n");
        }
        LOG.info(b.toString());
        reload(cfg);
    }

    private ResourceConfig createConfig() {
        ResourceConfig cfg = createNewResourceConfig(getRestExtensionsRootDir());


        cfg.register(JacksonFeature.class);
        LOG.info("Register Extension Info !!!");
        cfg.register(ExtensionInfoRest.class);

        return cfg;
    }



    private ResourceConfig createNewResourceConfig(File jp) {
        LOG.info("Local Path "+jp.getAbsolutePath());

        List<File> files = getJars(jp);

        ArrayList<URL> urls = new ArrayList<URL>();
        ArrayList<String> names = new ArrayList<String>();

        for (File f:files) {
            try {
                urls.add(f.toURI().toURL());
                names.add(f.getAbsolutePath());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        ClassLoader ccl =  AccessController.doPrivileged(ReflectionHelper.getContextClassLoaderPA());

        ClassLoader cl = new ChildFirstURLClassLoader(urls.toArray(new URL[]{}), ccl);


        ResourceConfig cfg = new ResourceConfig();
        cfg.setClassLoader(cl);



        ResourceFinder ps = new FilesScanner(names.toArray(new String[]{}),true);


        cfg.registerFinder(ps);
        return cfg;
    }

    private static final String CONTROLLER_HOME_PROPERTY_KEY = "appdynamics.controller.home";

    public static File getRestExtensionsRootDir()
    {
        File customDir = new File(System.getProperty(CONTROLLER_HOME_PROPERTY_KEY),"custom");
        return new File(customDir,"restExtensions");
    }

    private String getExtensionContentHash() {
        //TODO: Find a better hashing
        List<File> jars = getJars(getRestExtensionsRootDir());

        StringBuilder c = new StringBuilder("CH:");
        for (File jar : jars) {
            c.append(jar.getName()).append(":").append(jar.lastModified()).append(":").append(jar.length());
            c.append("\n");
        }
        return c.toString();
    }

    public static List<File> getJars(File jp) {
        ArrayList<File> erg = new ArrayList<File>();


        File[] jarfiles = jp.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".jar");
            }
        });

        if (jarfiles != null)  Collections.addAll(erg,jarfiles);


        return erg;

    }

    public Set<Class<?>> getLoaddedClasses() {
        return getConfiguration().getClasses();
    }

    private class CfgUpdater implements Runnable {
        private Comparable<String> _hash;

        public CfgUpdater(String extensionContentHash) {
            _hash = extensionContentHash;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(1000*60*1);
                    String newHash = getExtensionContentHash();
                    if (!newHash.equals(_hash)) {
                        updateResourceCfg();
                        _hash = newHash;
                    } else {
                        //TODO Remove
                        LOG.log(Level.SEVERE,"Update without success");
                    }
                } catch (InterruptedException e) {
                    LOG.log(Level.SEVERE,"Updater interupped, automatic updates stopped",e);
                }
            }
        }
    }

}
