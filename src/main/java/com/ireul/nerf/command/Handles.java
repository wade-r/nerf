package com.ireul.nerf.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Runtime wrapper for multiple {@link Handle}s
 *
 * @author Ryan Wade
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handles {
    Handle[] value();
}
