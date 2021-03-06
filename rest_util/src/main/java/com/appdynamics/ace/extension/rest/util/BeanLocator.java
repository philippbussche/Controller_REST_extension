package com.appdynamics.ace.extension.rest.util;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 23.10.13
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class BeanLocator {
    private static final String INTERNAL = "Internal";
    private static final String EJB_GLOBAL_LOOKUP = "java:global/controller/controller-beans/";
    private static final BeanLocator instance = new BeanLocator();

    public <T> T getGlobalBeanInstance(Class<T> clazz)
    {
        try
        {
            final InitialContext ctx = new InitialContext();
            final String name = parseClassName(clazz);
            return (T) ctx.lookup(EJB_GLOBAL_LOOKUP + name + "Bean" + "!"
                    + clazz.getName());
        }
        catch (NamingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<String> listAll(String filter)
    {
        try
        {
            ArrayList<String> result = new ArrayList<String>();

            final InitialContext ctx = new InitialContext();


            NamingEnumeration<NameClassPair> l = ctx.list(filter);

            while (l.hasMoreElements()) {
                NameClassPair p = l.nextElement();

                p.getName();
            }

            return result;
        }
        catch (NamingException e)
        {
            throw new RuntimeException(e);
        }
    }




    private <T> String parseClassName(Class<T> clazz)
    {
        String name = clazz.getSimpleName();
        if (name.startsWith("I"))
            name = name.substring(1);
        if (name.endsWith(INTERNAL))
            name = name.substring(0, name.length() - INTERNAL.length());
        return name;
    }


    public static BeanLocator getInstance() {
        return instance;
    }
}
