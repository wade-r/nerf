package com.ireul.nerf.web.route;

import java.util.HashMap;

/**
 * This class represents route matching result
 * <p>If {@link #route} is not null, means a {@link Route} is found and {@link #namedPaths} may contains named paths</p>
 *
 * @author Ryan Wade
 */
public class RouteResult {

    private HashMap<String, String> namedPaths = null;

    private Route route = null;

    /**
     * @return {@link #route}
     */
    public Route route() {
        return this.route;
    }

    /**
     * Set the {@link #route}
     *
     * @param route {@link Route} to set
     */
    public void route(Route route) {
        this.route = route;
    }

    /**
     * @return {@link #namedPaths}, create if not exists
     */
    public HashMap<String, String> namedPaths() {
        if (this.namedPaths == null) {
            this.namedPaths = new HashMap<>();
        }
        return this.namedPaths;
    }

    /**
     * @return if {@link #namedPaths} not null
     */
    public boolean hasNamedPaths() {
        return this.namedPaths != null;
    }

    /**
     * Reset, set {@link #route} to null and clear {@link #namedPaths} if not null
     */
    public void reset() {
        this.route = null;
        if (this.namedPaths != null) {
            this.namedPaths.clear();
        }
    }

}
