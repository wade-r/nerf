package com.ireul.nerf.web.route;

import java.util.HashMap;

/**
 * Created by ryanw on 2017/5/31.
 */
public class RouteMatch {

    private boolean matched = false;

    private HashMap<String, String> namedPaths = null;

    public RouteMatch matched(boolean matched) {
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
