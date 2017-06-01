package com.ireul.nerf.web.route;

import com.ireul.nerf.inject.Injector;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * SimpleRouter implements Router, search list of Route, execute first matches
 * Created by ryan on 5/31/17.
 */
public class SimpleRouter implements Router {

    private ArrayList<Route> routes;

    private Injector injector;

    public SimpleRouter(Collection<Route> routes, Injector injector) {
        this.routes = new ArrayList<>(routes);
        this.injector = injector;
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
                result.route(route);
                break;
            }
        }
        return result;
    }

}
