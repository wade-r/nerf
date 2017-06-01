package com.ireul.nerf.web.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Router is abstraction of Nerf routing system
 * Created by ryanw on 2017/5/31.
 */
public interface Router {

    /**
     * Handles a Servlet Request/Response pair
     *
     * @param request  servlet request
     * @param response servlet response
     * @return if route rule found and properly handled
     */
    boolean route(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
