package com.ireul.nerf.web.server;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * This class wraps a {@link HttpServletResponse}, providing various helper methods.
 *
 * @author Ryan Wade
 */
public class Response {

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String LOCATION = "Location";

    public static final String TEXT_PLAIN = "text/plain";

    public static final String TEXT_HTML = "text/html";

    public static final String APPLICATION_JSON = "application/json";

    private HttpServletResponse response;

    private HashMap<String, Object> locals;

    /**
     * Initialize with {@link HttpServletResponse}
     *
     * @param response servlet response
     */
    public Response(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Get the {@link HttpServletResponse}
     *
     * @return servlet response
     */
    public HttpServletResponse raw() {
        return this.response;
    }

    /**
     * Set response header
     *
     * @param name  header name
     * @param value header value
     */
    public void header(String name, String value) {
        raw().setHeader(name, value);
    }

    /**
     * Set status code
     *
     * @param status status code
     */
    public void status(int status) {
        raw().setStatus(status);
    }

    /**
     * Set Content-Type
     *
     * @param contentType content type
     */
    public void type(String contentType) {
        header(CONTENT_TYPE, contentType);
    }

    /**
     * Set Content-Length
     *
     * @param contentLength content length
     */
    public void length(long contentLength) {
        header(CONTENT_LENGTH, String.valueOf(contentLength));
    }

    /**
     * Get the body writer
     *
     * @return body writer
     */
    public PrintWriter bodyWriter() {
        try {
            return raw().getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write a string and close
     *
     * @param string string to write
     */
    public void body(String string) {
        PrintWriter writer = bodyWriter();
        writer.write(string);
        writer.close();
    }

    /**
     * Write chars and close
     *
     * @param chars chars to write
     */
    public void body(char[] chars) {
        PrintWriter writer = bodyWriter();
        writer.write(chars);
        writer.close();
    }

    /**
     * Send status code and body string
     *
     * @param code   status code
     * @param string body string
     */
    public void send(int code, String string) {
        status(code);
        body(string);
    }

    /**
     * Set Content-Type to text/plain and send string
     *
     * @param string content to body
     */
    public void bodyPlain(String string) {
        type(TEXT_PLAIN);
        length(string.length());
        body(string);
    }

    /**
     * Send status code and body string as text/plain
     *
     * @param code   status code
     * @param string body string
     */
    public void sendPlain(int code, String string) {
        status(code);
        bodyPlain(string);
    }

    /**
     * Serialize object to Json by Gson, set Content-Type to application/json
     *
     * @param object object to serialize
     */
    public void bodyJson(Object object) {
        type(APPLICATION_JSON);
        String json = new Gson().toJson(object);
        length(json.length());
        body(json);
    }

    /**
     * Send status code and body as application/json
     *
     * @param code   status code
     * @param object object as json
     */
    public void sendJson(int code, Object object) {
        status(code);
        bodyJson(object);
    }

    /**
     * Set Content-Type to text/html and send string
     *
     * @param html html to send
     */
    public void bodyHtml(String html) {
        type(TEXT_HTML);
        length(html.length());
        body(html);
    }

    /**
     * Set status to 301 or 302 and set Location header
     *
     * @param location location
     * @param status   status 301 or 302
     */
    public void redirect(String location, int status) {
        if (location == null) location = "/";
        status(status);
        header(LOCATION, location);
        body("Redirecting to " + location);
    }

    /**
     * Redirect with 302
     *
     * @param location location
     */
    public void redirect(String location) {
        redirect(location, HttpServletResponse.SC_MOVED_TEMPORARILY);
    }

    /**
     * Get all locals (for template rendering)
     *
     * @return all locals
     */
    public HashMap<String, Object> locals() {
        if (this.locals == null) {
            this.locals = new HashMap<>();
        }
        return this.locals;
    }

    /**
     * Set local value by name
     *
     * @param name  name of value
     * @param value the value
     */
    public void local(String name, Object value) {
        locals().put(name, value);
    }

    /**
     * Get local value
     *
     * @param name name of value
     * @param <T>  value type
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public <T> T local(String name) {
        return (T) locals().get(name);
    }

    /**
     * Set encoding of request
     *
     * @param encoding encoding
     */
    public void encoding(String encoding) {
        raw().setCharacterEncoding(encoding);
    }

}
