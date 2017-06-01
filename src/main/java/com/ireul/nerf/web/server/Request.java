package com.ireul.nerf.web.server;

import org.eclipse.jetty.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * This class wraps a {@link HttpServletRequest}, providing many helper methods
 *
 * @author Ryan Wade
 */
public class Request {

    private static final String NAMED_PATHS = "com.ireul.nerf.NamedPaths";

    private final HttpServletRequest request;

    /**
     * Initialize Request with a {@link HttpServletRequest}
     *
     * @param request new Request
     */
    public Request(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Set named paths
     *
     * @param namedPaths named paths HashMap to set
     */
    public void namedPaths(HashMap<String, String> namedPaths) {
        this.request.setAttribute(NAMED_PATHS, namedPaths);
    }

    /**
     * Get all named paths
     *
     * @return all named paths
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> namedPaths() {
        HashMap<String, String> namedPaths = (HashMap<String, String>) this.request.getAttribute(NAMED_PATHS);
        if (namedPaths == null) {
            namedPaths = new HashMap<>();
            namedPaths(namedPaths);
        }
        return namedPaths;
    }

    /**
     * Get named path by name
     *
     * @param name name of named path
     * @return value of named path
     */
    public String namedPath(String name) {
        return namedPaths().get(name);
    }

    /**
     * Get the internal {@link HttpServletRequest}
     *
     * @return servlet request
     */
    public HttpServletRequest raw() {
        return this.request;
    }

    /**
     * Get the http method
     *
     * @return http method
     */
    public HttpMethod method() {
        return HttpMethod.fromString(raw().getMethod());
    }

    /**
     * Get the query string
     *
     * @return query string
     */
    public String queryString() {
        return raw().getQueryString();
    }

}
