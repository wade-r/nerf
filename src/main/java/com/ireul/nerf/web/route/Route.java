package com.ireul.nerf.web.route;

import com.ireul.nerf.web.controller.Controller;

import java.lang.reflect.Method;

/**
 * This class represents a rule of routing and how to execute corresponding controller method
 *
 * @author Ryan Wade
 * @see Action
 * @see Controller
 */
public class Route {

    private final Action action;

    private final Class<? extends Controller> controllerClass;

    private final Method method;

    public Action action() {
        return this.action;
    }

    public Class<? extends Controller> controllerClass() {
        return this.controllerClass;
    }

    public Method method() {
        return this.method;
    }

    /**
     * Initialize a {@link Route}
     *
     * @param controllerClass the controller class, commonly subclass of {@link com.ireul.nerf.web.controller.BaseController}
     * @param method          the instance method to invoke
     * @param action          the {@link Action} to route
     */
    public Route(Class<? extends Controller> controllerClass, Method method, Action action) {
        this.action = action;
        this.controllerClass = controllerClass;
        this.method = method;
    }

}
