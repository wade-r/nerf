package com.ireul.nerf.web.route;

import org.eclipse.jetty.http.HttpMethod;

import java.lang.annotation.*;

/**
 * This annotation marks a instance method on {@link com.ireul.nerf.web.controller.Controller} as a action entry.
 * <p><b>Marked methods MUST NOT have parameters and MUST NOT have returns.</b></p>
 * <p>Multiple {@link Action} can be marked on the same method.</p>
 *
 * @author Ryan Wade
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Actions.class)
public @interface Action {
    /**
     * Path pattern
     *
     * @return path pattern
     */
    String value();

    /**
     * Accepted http methods, default to GET
     *
     * @return accepted http methods
     */
    HttpMethod[] method() default {HttpMethod.GET};
}
