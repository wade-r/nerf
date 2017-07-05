package com.ireul.nerf.web.route;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class provides basic implementation of {@link Router}, iterate all {@link #routes} and returns first match
 *
 * @author Ryan Wade
 */
public class SimpleRouter implements Router {

    private ArrayList<Route> routes;

    public SimpleRouter(Collection<Route> routes) {
        this.routes = new ArrayList<>(routes);
    }

    private boolean matchRoute(Route route, HttpServletRequest request, RouteResult result) {
        // check method
        if (Arrays.stream(route.action().method()).noneMatch(m -> m.is(request.getMethod()))) {
            return false;
        }

        // check path
        URI uri = URI.create(request.getRequestURL().toString());
        return RouteUtils.matchPath(route.action().value(), uri.getPath(), result);
    }

    @Override
    public RouteResult route(HttpServletRequest request) {
        RouteResult result = new RouteResult();
        for (Route route : this.routes) {
            result.reset();
            if (this.matchRoute(route, request, result)) {
                result.setRoute(route);
                break;
            }
        }
        return result;
    }

}
