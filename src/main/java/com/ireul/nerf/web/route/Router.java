package com.ireul.nerf.web.route;

import com.ireul.nerf.utils.AnnotationUtils;
import com.ireul.nerf.web.controller.Controller;
import org.reflections.Reflections;

import java.util.ArrayList;

/**
 * Created by ryan on 5/31/17.
 */
public class Router {

    private final ArrayList<Route> routes = new ArrayList<>();

    public static Router scan(String base) {
        Router router = new Router();
        Reflections reflections = new Reflections(base);
        reflections.getSubTypesOf(Controller.class).forEach(c -> {
            AnnotationUtils.findInstanceMethod(c, Action.class, (method, action) -> {
                router.routes().add(new Route(c, method, action));
            });
        });
        return router;
    }

    public ArrayList<Route> routes() {
        return routes;
    }

}
