package com.ireul.nerf.web.route;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface abstracts routing system
 *
 * @author Ryan Wade
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
