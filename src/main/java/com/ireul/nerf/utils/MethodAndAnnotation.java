package com.ireul.nerf.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * This is a container class for a Method / Annotation pair
 *
 * @param <A> annotation class
 * @author Ryan Wade
 */
public class MethodAndAnnotation<A extends Annotation> {

    public final Method method;

    public final A annotation;

    public MethodAndAnnotation(Method method, A annotation) {
        this.method = method;
        this.annotation = annotation;
    }
}
