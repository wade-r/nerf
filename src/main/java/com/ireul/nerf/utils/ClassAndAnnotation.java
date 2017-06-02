package com.ireul.nerf.utils;

import java.lang.annotation.Annotation;

/**
 * Container for a Class / Annotation pair
 *
 * @author Ryan Wade
 */
public class ClassAndAnnotation<C, A extends Annotation> {

    public final Class<? extends C> classType;

    public final A annotation;

    public ClassAndAnnotation(Class<? extends C> classType, A annotation) {
        this.classType = classType;
        this.annotation = annotation;
    }
}
