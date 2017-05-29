package com.ireul.nerf.web.controller;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ryan on 5/27/17.
 */
public interface Controller {

    String CONTENT_TYPE = "Content-Type";

    String LOCATION = "Location";

    String TEXT_PLAIN = "text/plain";

    String TEXT_HTML = "text/html";

    String APPLICATION_JSON = "application/json";

    /*******************************************************************************************************************
     * Context accessing
     ******************************************************************************************************************/

    HttpServletRequest request();

    HttpServletResponse response();

    void request(HttpServletRequest request);

    void response(HttpServletResponse response);

    void local(String key, Object value);

    Object local(String key);

    /*******************************************************************************************************************
     * Life Cycle
     ******************************************************************************************************************/

    void beforeAction();

    /*******************************************************************************************************************
     * Request
     ******************************************************************************************************************/

    default String method() {
        return request().getMethod();
    }

    default boolean isGet() {
        return method().equalsIgnoreCase("GET");
    }

    default boolean isPost() {
        return method().equalsIgnoreCase("POST");
    }

    default boolean isDelete() {
        return method().equalsIgnoreCase("DELETE");
    }

    default boolean isPut() {
        return method().equalsIgnoreCase("PUT");
    }

    default boolean isPatch() {
        return method().equalsIgnoreCase("PATCH");
    }

    default boolean isUpdate() {
        return isPatch() || isPut();
    }

    default String queryString() {
        return request().getQueryString();
    }

    default String url() {
        return request().getRequestURI();
    }

    /*******************************************************************************************************************
     * Response
     ******************************************************************************************************************/

    default void header(String name, String value) {
        response().setHeader(name, value);
    }

    default void statusCode(int statusCode) {
        response().setStatus(statusCode);
    }

    default void contentType(String contentType) {
        header(CONTENT_TYPE, contentType);
    }

    default void redirect(String location, int statusCode) {
        if (location == null) {
            location = "/";
        }
        header(LOCATION, location);
        statusCode(statusCode);
        bodyPlain("Redirect to " + location);
    }

    default void redirect(String location) {
        redirect(location, HttpServletResponse.SC_MOVED_TEMPORARILY);
    }

    default void body(char[] bytes) {
        PrintWriter writer;
        try {
            writer = response().getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        writer.write(bytes);
        writer.close();
    }

    default void body(String string) {
        PrintWriter writer;
        try {
            writer = response().getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        writer.write(string);
        writer.close();
    }

    default void bodyPlain(String string) {
        contentType(TEXT_PLAIN);
        body(string);
    }

    default void bodyJson(Object object) {
        contentType(APPLICATION_JSON);
        Gson gson = new Gson();
        body(gson.toJson(object));
    }

    default void bodyHTML(String string) {
        contentType(TEXT_HTML);
        body(string);
    }

}
