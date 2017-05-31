package com.ireul.nerf.web.server;

import com.ireul.nerf.web.route.Route;
import org.eclipse.jetty.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Request wraps a {@link HttpServletRequest}
 * <p>
 * Created by ryan on 5/31/17.
 */
public class Request {

    private final HttpServletRequest request;

    /**
     * Initialize Request with a {@link HttpServletRequest}
     *
     * @param request new Request
     */
    public Request(HttpServletRequest request) {
        this.request = request;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> namedPaths() {
        HashMap<String, String> attribute = (HashMap<String, String>) this.request.getAttribute(Route.class.getCanonicalName());
        if (attribute == null) {
            attribute = new HashMap<>();
            this.request.setAttribute(Route.class.getCanonicalName(), attribute);
        }
        return attribute;
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
