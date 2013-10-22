package com.appdynamics.ace.extension.rest.util;

import com.appdynamics.ace.extension.rest.command.api.Constants;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 16.10.13
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */

@WebFilter ("/"+ Constants.PREFIX+"/*")
public class BasicAuthenticationFilter implements Filter {
    private static final long serialVersionUID = 1L;

    private Logger _logger = Logger.getLogger(BasicAuthenticationFilter.class.getName());


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        _logger.log(Level.INFO,"TESTER");
        _logger.log(Level.INFO,"T:"+request.getClass().getName());

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;


        _logger.log(Level.INFO,"NAME:::"+httpReq.getServerName());


        String basicAuth = httpReq.getHeader("Authorization");


        if (basicAuth != null) {

            if (basicAuth.startsWith("Basic")) {
                String decoded = null;
                decoded = new String(new Base64().decode(basicAuth.substring(6).getBytes()));

                String[] parts = decoded.split("@|:");
                _logger.log(Level.INFO, parts.toString());
                if (parts.length == 3) {

                    HttpSession session = httpReq.getSession(true);
                    if (session != null) {
                        String userAccount = parts[0].concat("@").concat(parts[1]);
                        try {
                            httpReq.login(userAccount,parts[2]);
                            _logger.log(Level.INFO, "Session created... returning SC_OK");
                        }
                        catch(ServletException se) {
                            _logger.log(Level.INFO, "HttpServletRequest.login failed", se);
                        }
                    }
                } else {
                    _logger.log(Level.INFO,"Malformed basic auth string; returning custom status 499");
                }
            }
        }  else {
            _logger.log(Level.INFO,"Basic authorization not provided; returning custom status 499");
        }



        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
