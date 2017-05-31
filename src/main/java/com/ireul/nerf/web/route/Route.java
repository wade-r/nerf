package com.ireul.nerf.web.route;

import com.ireul.nerf.web.controller.Controller;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        return true;
    }

    public void execute(Request request, HttpServletResponse response) {
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
