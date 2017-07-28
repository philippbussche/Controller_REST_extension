/*
 * Copyright (c) AppDynamics, Inc., and its affiliates
 * 2016
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF APPDYNAMICS, INC.
 * The copyright notice above does not evidence any actual or intended publication of such source code
 */

package com.appdynamics.ace.extension.rest.util;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Helper class for doing Glassfish programmatic login.
 *
 * Created by fgonzales on 5/16/16.
 */
public class ProgrammaticLoginHelper {
    private static final Logger logger = Logger.getLogger(ProgrammaticLoginHelper.class.getName());

    private static final String GLASSFISH3_PROGRAMMATIC_LOGIN_CLASS =
            "com.sun.appserv.security.ProgrammaticLogin";
    private static final String GLASSFISH4_PROGRAMMATIC_LOGIN_CLASS =
            "com.sun.enterprise.security.ee.auth.login.ProgrammaticLogin";

    /**
     * Attempts to login a user.
     *
     * @param usernameAndAccountUrlEncoded URL encoded username and account name (e.g. user1@customer1)
     * @param passwordURLEncoded URL encoded password
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @return <code>true</code> if login was successful, <code>false</code> otherwise
     *
     * @throws Exception any exception encountered during login
     */
    public static boolean login(String usernameAndAccountUrlEncoded, char[] passwordURLEncoded,
                                HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // CORE-37436: We need to recreate session right before authentication for security reasons.
        destroySessionIfExists(request);

        // Create the session before login. Otherwise the user principal is not cached correctly
        request.getSession(true);

        //ProgrammaticLogin programmaticLogin = new ProgrammaticLogin();
        // return programmaticLogin.login(userAndAccountURLEncoded,
        //        passwordURLEncoded, AuthRealm.JAAS_CONTEXT_PARAM, request, response, true);

        // Invoke the above statements via reflection, since the package name has
        // changed between Glassfish 3.1 and Glassfish 4.1

        Class<?> programmaticLoginClass = getProgrammaticLoginClass();
        Object programmaticLogin = programmaticLoginClass.newInstance();

        Class[] argTypes = new Class[] {
                String.class,              // user
                char[].class,              // password
                String.class,              // realm
                HttpServletRequest.class,  // request
                HttpServletResponse.class, // response
                boolean.class};            // boolean

        Object[] args = new Object[] {usernameAndAccountUrlEncoded, passwordURLEncoded,
                "controller_realm", request, response, true};

        Method loginMethod  = programmaticLoginClass.getDeclaredMethod("login", argTypes);

        boolean loginSuccess = (Boolean) loginMethod.invoke(programmaticLogin, args);

        // Log authentication result (CORE-51618)
        String usernameAndAccount = URLDecoder.decode(usernameAndAccountUrlEncoded, "UTF-8");
        if (loginSuccess) {
            String message = "ID000066 User [" + usernameAndAccount + "] logged in successfully";
            logger.info(message);
        } else {
            String message = "ID000066 User [" + usernameAndAccount + "] failed to log in";
            logger.info(message);
        }

        return loginSuccess;
    }

    /**
     * Logs out a user.
     *
     * @throws Exception any exception encountered during logout
     */
    public static void logout() throws Exception {
        // ProgrammaticLogin programmaticLogin = new ProgrammaticLogin();
        // programmaticLogin.logout();

        // Invoke the above statements via reflection, since the package name has
        // changed between Glassfish 3.1 and Glassfish 4.1

        Class<?> programmaticLoginClass = getProgrammaticLoginClass();
        Object programmaticLogin = programmaticLoginClass.newInstance();

        Method logoutMethod  = programmaticLoginClass.getDeclaredMethod("logout");
        logoutMethod.invoke(programmaticLogin);
    }

    public static void destroySessionIfExists(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            logger.log(Level.INFO, "ID000066 Invalidating session");
            session.invalidate();
        }
    }

    /**
     * Returns the <code>ProgrammaticLogin</code> class corresponding to the current
     * version of the Glassfish application server.
     *
     * @return the <code>ProgrammaticLogin</code> class
     * @throws ClassNotFoundException if the <code>ProgrammaticLogin</code> class can't be found
     */
    private static Class<?> getProgrammaticLoginClass() throws ClassNotFoundException {
        Class<?> programmaticLoginClass;
        try {
            // Try the class name for Glassfish 3 first
            programmaticLoginClass = Class.forName(GLASSFISH3_PROGRAMMATIC_LOGIN_CLASS);
        } catch (ClassNotFoundException e) {
            // No problem. Assume we are running on Glassfish 4
            programmaticLoginClass = Class.forName(GLASSFISH4_PROGRAMMATIC_LOGIN_CLASS);
        }
        return programmaticLoginClass;
    }
}
