package com.ireul.nerf.web.route;

import java.util.HashMap;

/**
 * Route rule evaluation result
 * Created by ryanw on 2017/5/31.
 */
public class RouteResult {

    public void reset() {
        this.route = null;
        if (this.namedPaths != null) {
            this.namedPaths.clear();
        }
    }

    private HashMap<String, String> namedPaths = null;

    private Route route = null;

    public Route route() {
        return this.route;
    }

    public void route(Route route) {
        this.route = route;
    }

    public HashMap<String, String> namedPaths() {
        if (this.namedPaths == null) {
            this.namedPaths = new HashMap<>();
        }
        return this.namedPaths;
    }

    public boolean hasNamedPaths() {
        return this.namedPaths != null;
    }

}
