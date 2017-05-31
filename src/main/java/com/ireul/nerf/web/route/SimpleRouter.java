package com.ireul.nerf.web.route;

import com.ireul.nerf.web.controller.Controller;
import com.ireul.nerf.web.server.Request;
import com.ireul.nerf.web.server.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
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

    public SimpleRouter(Collection<Route> routes) {
        this.routes = new ArrayList<>(routes);
    }

    public static SimpleRouter scan(String basePackage) {
        return new SimpleRouter(RouteUtils.scanRoutes(basePackage));
    }

    private void matchRoute(Route route, HttpServletRequest request, RouteMatch output) {
        // check method
        if (Arrays.stream(route.action().method()).noneMatch(m -> m.is(request.getMethod()))) {
            output.matched(false);
            return;
        }

        // check path
        URI uri = URI.create(request.getRequestURL().toString());
        RouteUtils.matchPath(route.action().value(), uri.getPath(), output);
    }

    @Override
    public boolean route(HttpServletRequest request, HttpServletResponse response) {
        // match is shared, preventing massive allocation
        RouteMatch match = new RouteMatch();
        for (Route route : this.routes) {
            matchRoute(route, request, match);
            if (match.matched()) {
                try {
                    // create wrapped request and set namedPaths
                    Request wrappedRequest = new Request(request);
                    wrappedRequest.namedPaths(match.namedPaths());
                    // create wrapped response
                    Response wrappedResponse = new Response(response);
                    // initialize a controller and set request/response
                    Controller controller = route.controllerClass().newInstance();
                    controller.request(wrappedRequest);
                    controller.response(wrappedResponse);
                    // invoke beforeAction
                    controller.beforeAction();
                    // invoke method
                    route.method().invoke(controller);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

}
