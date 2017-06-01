package com.ireul.nerf.web.route;

import com.ireul.nerf.utils.AnnotationUtils;
import com.ireul.nerf.web.controller.Controller;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanw on 2017/5/31.
 */
public class RouteUtils {

    /**
     * Scan all implementation of Controller in given package, and get all methods annotated with @Action
     *
     * @param basePackage base package to search
     * @return list of routes
     */
    public static List<Route> scanPackage(String basePackage) {
        ArrayList<Route> routes = new ArrayList<>();
        Reflections reflections = new Reflections(basePackage);
        reflections.getSubTypesOf(Controller.class).forEach(controllerClass -> {
            AnnotationUtils.forEachInstanceMethod(
                    controllerClass,
                    Action.class,
                    (method, action) -> routes.add(new Route(controllerClass, method, action))
            );
        });
        return routes;
    }

    public static List<Route> scanClass(Class<?> clazz) {
        return scanPackage(clazz.getPackage().getName());
    }

    /**
     * Match a path with pattern, ":name" are supported
     *
     * @param pattern pattern to match
     * @param path    path to match
     * @param output  the output {@link RouteResult}
     */
    public static boolean matchPath(String pattern, String path, RouteResult output) {
        if (pattern == null || path == null) {
            return false;
        }

        String[] patterns = pattern.split("/");
        String[] paths = path.split("/");

        // length not equal, return directly
        if (patterns.length != paths.length) {
            return false;
        }

        for (int i = 0; i < patterns.length; i++) {
            String p = patterns[i];
            String a = paths[i];

            if (p.startsWith(":")) {
                // match ":name", put named path
                output.namedPaths().put(p.substring(1), a);
            } else if (!p.equals(a)) {
                // not equal, clearNamedPaths and return
                return false;
            } // else, keep iterating
        }

        return true;
    }

    /**
     * Match two mime type, provide match result
     *
     * @param acceptMime mime from HTTP Accept header, format ";q=xx" is ignored, comma separated
     * @param mime       mime Action provides
     * @return matching result, larger is better
     */
    public static float matchMime(String acceptMime, String mime) {
        if (acceptMime == null || mime == null) return 0;

        // trim mime
        mime = mime.trim().toLowerCase();

        float best = 0;

        // split acceptMime
        String[] acceptMimes = acceptMime.split(",");
        // split mime primary/secondary
        String[] ms = mime.split("/");

        // returns 0 if not well formatted
        if (ms.length != 2) {
            return 0;
        }

        for (String a : acceptMimes) {
            // ignore ";q=?"
            a = a.trim().split(";")[0].trim().toLowerCase();

            // found full equal, directly return 1
            if (a.equals(mime)) {
                return 1;
            }

            // split acceptMime primary/secondary
            String[] as = a.split("/");

            // skip if not well formatted
            if (as.length != 2) {
                continue;
            }

            float result = 0;

            if (ms[0].equals("*") && ms[1].equals("*")) {
                result = 0.2f;
            } else if (ms[0].equals(as[0]) && ms[1].equals("*")) {
                result = 0.4f;
            } else if (ms[1].equals(as[1]) && ms[0].equals("*")) {
                result = 0.5f;
            } else if (ms[0].equals(as[0]) && (as[1].endsWith("+" + ms[1]) || as[1].startsWith(ms[1] + "+"))) {
                result = 0.7f;
            }

            if (result > best) best = result;
        }
        return best;
    }

}
