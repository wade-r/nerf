package com.ireul.nerf.utils;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * Container for a Class / Annotation pair
 *
 * @author Ryan Wade
 */
public class AnnotatedClass<A extends Annotation, C> {

    private final Class<? extends C> classType;

    private final A annotation;

    public AnnotatedClass(@NotNull Class<? extends C> classType, @NotNull A annotation) {
        Objects.requireNonNull(classType);
        Objects.requireNonNull(annotation);
        this.classType = classType;
        this.annotation = annotation;
    }

    @NotNull
    public Class<? extends C> getClassType() {
        return classType;
    }

    @NotNull
    public A getAnnotation() {
        return annotation;
    }

}
