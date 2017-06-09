package com.ireul.nerf.web.server;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
     * Get header value by name
     *
     * @param name name
     * @return header value
     */
    public String header(String name) {
        return raw().getHeader(name);
    }

    /**
     * Get the query string
     *
     * @return query string
     */
    public String queryString() {
        return raw().getQueryString();
    }

    /**
     * Get parameter by name
     *
     * @param name name of parameter
     * @return parameter value
     */
    public String param(String name) {
        return raw().getParameter(name);
    }

    /**
     * Get parameter map
     *
     * @return parameter map
     */
    public Map<String, String[]> params() {
        return raw().getParameterMap();
    }

    /**
     * Get the {@link BufferedReader} of request body
     *
     * @return request body reader
     */
    public BufferedReader body() {
        try {
            return raw().getReader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the request body as string
     *
     * @return request body
     */
    public String bodyString() {
        try {
            return IOUtils.toString(body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set encoding of request
     *
     * @param encoding encoding
     */
    public void encoding(String encoding) {
        try {
            raw().setCharacterEncoding(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get encoding of request
     *
     * @return encoding of request
     */
    public String encoding() {
        return raw().getCharacterEncoding();
    }

}
