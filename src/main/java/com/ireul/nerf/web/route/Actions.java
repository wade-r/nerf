package com.ireul.nerf.web.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Java runtime wrapper for multiple {@link Action}
 *
 * @author Ryan Wade
 * @see Action
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Actions {
    Action[] value() default {};
}
