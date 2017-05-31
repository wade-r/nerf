package com.ireul.nerf.web.route;

import org.eclipse.jetty.http.HttpMethod;

import java.lang.annotation.*;

/**
 * Created by ryan on 5/30/17.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Actions.class)
public @interface Action {
    String value();

    HttpMethod[] method() default {HttpMethod.GET};

    String allowedType() default "*/*";
}
