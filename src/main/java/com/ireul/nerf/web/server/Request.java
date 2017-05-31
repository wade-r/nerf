package com.ireul.nerf.web.server;

import org.eclipse.jetty.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Request wraps a {@link HttpServletRequest}
 * <p>
 * Created by ryan on 5/31/17.
 */
public class Request {

    public static final String NAMED_PATHS = "com.ireul.nerf.NamedPaths";

    private final HttpServletRequest request;

    /**
     * Initialize Request with a {@link HttpServletRequest}
     *
     * @param request new Request
     */
    public Request(HttpServletRequest request) {
        this.request = request;
    }

    public void namedPaths(HashMap<String, String> namedPaths) {
        this.request.setAttribute(NAMED_PATHS, namedPaths);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> namedPaths() {
        HashMap<String, String> namedPaths = (HashMap<String, String>) this.request.getAttribute(NAMED_PATHS);
        if (namedPaths == null) {
            namedPaths = new HashMap<>();
            namedPaths(namedPaths);
        }
        return namedPaths;
    }

    public String namedPath(String name) {
        return namedPaths().get(name);
    }

    public HttpServletRequest raw() {
        return this.request;
    }

    public HttpMethod method() {
        return HttpMethod.fromString(raw().getMethod());
    }

    public String queryString() {
        return raw().getQueryString();
    }

}
