package com.ireul.nerf.web.controller;

/**
 * This exception is used for halt a http action intentionally
 * <p>Example: </p>
 * <pre>
 *     void action() throws Halt {
 *         type("text/plain");
 *         if (namedPath("url") == null) {
 *             throw new Halt(404, "url not found");
 *             // or halt(404, "url not found");
 *         }
 *         body("alert('hello');");
 *     }
 * </pre>
 *
 * @author Ryan Wade
 */
public class Halt extends Exception {

    private final int code;

    private final String body;

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public Halt(int code, String body) {
        super("Intentionally Halt");
        this.code = code;
        this.body = body;
    }

}
