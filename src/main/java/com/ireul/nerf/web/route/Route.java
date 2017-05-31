package com.ireul.nerf.web.route;

import com.ireul.nerf.web.controller.Controller;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ryan on 5/31/17.
 */
public class Route {

    private final Action action;

    private final Class<? extends Controller> controllerClass;

    private final Method method;

    private HashMap<String, String> namedParams;

    public HashMap<String, String> namedParams() {
        if (this.namedParams == null)
            this.namedParams = new HashMap<>();
        return this.namedParams;
    }

    public Route(Class<? extends Controller> controllerClass, Method method, Action action) {
        this.action = action;
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public boolean match(Request request) {
        // check method
        if (Arrays.stream(action.method()).noneMatch(m -> m.is(request.getMethod()))) {
            return false;
        }

        // check path components
        String[] components = request.getHttpURI().getPath().split("/");
        String[] matchComponents = this.action.value().split("/");

        if (components.length != matchComponents.length) {
            return false;
        }

        for (int i = 0; i < components.length; i++) {
            String matchComponent = matchComponents[i];
            String component = components[i];

            if (matchComponent.startsWith(":")) {
                namedParams().put(matchComponent.substring(1), component);
            } else {
                if (!matchComponent.equals(component)) {
                    return false;
                }
            }
        }

        // check accept
        String accept = request.getHeader("Accept");
        if (accept == null || accept.length() == 0) {
            return true;
        }
        String[] accepts = accept.split(",");
        String[] matchAccepts = this.action.accept().split(",");
        for (String a : accepts) {
            // trim "text/plain;q=0.x" style
            a = a.split(";")[0];
            for (String m : matchAccepts) {
                String[] as = a.trim().split("/");
                String[] ms = m.trim().split("/");
                if (as.length == 2 && ms.length == 2) {
                    return (ms[0].equals("*") || ms[0].equalsIgnoreCase(as[0])) && (ms[1].equals("*") || ms[1].equalsIgnoreCase(as[1]));
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Controller controller = this.controllerClass.newInstance();
            controller.request(request);
            controller.response(response);
            controller.beforeAction();
            this.method.invoke(controller);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
