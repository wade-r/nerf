package com.ireul.nerf.web.route;

import javax.servlet.http.HttpServletRequest;

/**
 * Router is abstraction of Nerf routing system
 * Created by ryanw on 2017/5/31.
 */
public interface Router {

    /**
     * Search a Route by given request, wrapped in RouteResult with extra values
     *
     * @param request request to search
     * @return RouteResult, route is null if not found
     */
    RouteResult route(HttpServletRequest request);

}
