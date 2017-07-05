package com.ireul.nerf.web.controller;

import javax.validation.constraints.NotNull;

import static com.ireul.nerf.utils.Mimes.TEXT_PLAIN;

/**
 * This exception is used for halt a http action intentionally
 * <p>Example: </p>
 * <pre>
 *     void action() throws Halt {
 *         type("text/plain");
 *         if (namedPath("url") == null) {
 *             throw new Halt(404, "url not found");
 *         }
 *         body("alert('hello');");
 *     }
 * </pre>
 *
 * @author Ryan Wade
 */
@SuppressWarnings("WeakerAccess unused")
public class Halt extends RuntimeException {

    private final int statusCode;

    private final String body;

    private final String contentType;

    @NotNull
    public int getStatusCode() {
        return statusCode;
    }

    @NotNull
    public String getBody() {
        return body;
    }

    @NotNull
    public String getContentType() {
        return contentType;
    }

    public Halt() {
        this(null);
    }

    public Halt(String body) {
        this(400, body);
    }

    public Halt(int statusCode, String body) {
        this(statusCode, body, null);
    }

    public Halt(int statusCode, String body, String contentType) {
        super("Intentionally Halt");

        if (body == null) {
            body = "Invalid Request";
        }
        if (contentType == null) {
            contentType = TEXT_PLAIN;
        }
        this.statusCode = statusCode;
        this.body = body;
        this.contentType = TEXT_PLAIN;
    }

}
