package com.ireul.nerf.utils;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * This is a container class for a Method / Annotation pair
 *
 * @param <A> annotation class
 * @author Ryan Wade
 */
public class AnnotatedMethod<A extends Annotation> {

    public final Method method;

    public final A annotation;

    public AnnotatedMethod(@NotNull Method method, @NotNull A annotation) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(annotation);
        this.method = method;
        this.annotation = annotation;
    }

    @NotNull
    public Method getMethod() {
        return method;
    }

    @NotNull
    public A getAnnotation() {
        return annotation;
    }

}
