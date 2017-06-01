package com.ireul.nerf.web.route;

import java.util.HashMap;

/**
 * Route rule evaluation result
 * Created by ryanw on 2017/5/31.
 */
public class RouteResult {

    private boolean matched = false;

    private HashMap<String, String> namedPaths = null;

    public RouteResult matched(boolean matched) {
        this.matched = matched;
        return this;
    }

    public boolean matched() {
        return this.matched;
    }

    public void clearNamedPaths() {
        this.matched = false;
        if (this.namedPaths != null) {
            this.namedPaths.clear();
        }
    }

    public HashMap<String, String> namedPaths() {
        if (this.namedPaths == null) {
            this.namedPaths = new HashMap<>();
        }
        return this.namedPaths;
    }

}
