package com.ireul.nerf.web.route;

import com.ireul.nerf.web.controller.Controller;

import java.lang.reflect.Method;

/**
 * Route is composed with matching rules and a target class and a target {@link Method}
 * Created by ryan on 5/31/17.
 */
public class Route {

    private final Action action;

    private final Class<? extends Controller> controllerClass;

    private final Method method;

    public Action action() {
        return action;
    }

    public Class<? extends Controller> controllerClass() {
        return controllerClass;
    }

    public Method method() {
        return method;
    }

    public Route(Class<? extends Controller> controllerClass, Method method, Action action) {
        this.action = action;
        this.controllerClass = controllerClass;
        this.method = method;
    }

}
