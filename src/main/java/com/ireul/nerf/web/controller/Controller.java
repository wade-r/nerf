package com.ireul.nerf.web.controller;

import com.ireul.nerf.web.WebContext;
import com.ireul.nerf.web.server.Request;
import com.ireul.nerf.web.server.Response;
import org.eclipse.jetty.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * This interface is for abstracting a web controller.
 * <p><b>Most people may want to subclass {@link BaseController}, rather than implement this interface.</b></p>
 * <p>This interface provides many handy methods</p>
 *
 * @author Ryan Wade
 * @see BaseController
 */
public interface Controller {

    /**
     * Get the {@link Request}
     *
     * @return request
     */
    Request request();

    /**
     * Get the {@link Response}
     *
     * @return response
     */
    Response response();

    /**
     * Set the {@link Request}
     *
     * @param request request
     */
    void request(Request request);

    /**
     * Set the {@link Response}
     *
     * @param response response
     */
    void response(Response response);

    /**
     * Return the web context
     *
     * @return current {@link WebContext}
     */
    WebContext context();

    /**
     * This will be invoked before action
     */
    void beforeAction();

    /**
     * Get request http method
     *
     * @return request http method
     * @see Request#method()
     */
    default HttpMethod method() {
        return request().method();
    }

    /**
     * Check request method
     *
     * @return request is a GET request
     * @see Request#method()
     */
    default boolean isGet() {
        return HttpMethod.GET == method();
    }

    /**
     * Check request method
     *
     * @return request is a POST request
     * @see Request#method()
     */
    default boolean isPost() {
        return HttpMethod.POST == method();
    }

    /**
     * Check request method
     *
     * @return request is a DELETE request
     * @see Request#method()
     */
    default boolean isDelete() {
        return HttpMethod.DELETE == method();
    }

    /**
     * Check request method
     *
     * @return request is a PUT request
     * @see Request#method()
     */
    default boolean isPut() {
        return HttpMethod.PUT == method();
    }

    /**
     * Get request query string
     *
     * @return query string
     * @see Request#queryString()
     */
    default String queryString() {
        return request().queryString();
    }

    /**
     * Get parameter by name
     *
     * @param name name of parameter
     * @return parameter value
     */
    default String param(String name) {
        return request().param(name);
    }

    /**
     * Get all parameters
     *
     * @return parameters as Map
     */
    default Map<String, String[]> params() {
        return request().params();
    }

    /**
     * Get named paths
     *
     * @return named paths
     * @see Request#namedPaths()
     */
    default HashMap<String, String> namedPaths() {
        return request().namedPaths();
    }

    /**
     * Get named path by name
     *
     * @param name name
     * @return value of named path
     * @see Request#namedPaths()
     */
    default String namedPath(String name) {
        return request().namedPath(name);
    }

    /**
     * Set response header
     *
     * @param name  header name
     * @param value header value
     * @see Response#header(String, String)
     */
    default void header(String name, String value) {
        response().header(name, value);
    }

    /**
     * Set response status
     *
     * @param status status code
     * @see Response#status(int)
     */
    default void status(int status) {
        response().status(status);
    }

    /**
     * Set response content type
     *
     * @param type content type
     */
    default void type(String type) {
        response().type(type);
    }

    /**
     * Set response content length
     *
     * @param length content length
     */
    default void length(long length) {
        response().length(length);
    }

    default void body(String body) {
        response().body(body);
    }

    default void body(char[] chars) {
        response().body(chars);
    }

    default void bodyPlain(String string) {
        response().bodyPlain(string);
    }

    default void bodyJson(Object object) {
        response().bodyJson(object);
    }

    default void bodyHtml(String html) {
        response().bodyHtml(html);
    }

    default void redirect(String location, int status) {
        response().redirect(location, status);
    }

    default void redirect(String location) {
        response().redirect(location);
    }

    default HashMap<String, Object> locals() {
        return response().locals();
    }

    default void local(String name, Object value) {
        response().local(name, value);
    }

    default <T> T local(String name) {
        return response().local(name);
    }

    default void renderHtml(String template) {
        type("text/html");
        render(template);
    }

    default void render(String template) {
        context().render(template, response().locals(), response().bodyWriter());
    }

}
