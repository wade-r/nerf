package com.ireul.nerf.utils;

import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * This is a helper class providing annotation related methods.
 *
 * @author Ryan Wade
 */
public class AnnotationUtils {

    /**
     * Create a stream with all instanced methods with specified annotation, support Repeatable annotation
     *
     * @param clazz          class to search
     * @param annotationType annotation type
     * @param <T>            annotation
     * @param <U>            class type
     * @return stream
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation, U> Stream<MethodAndAnnotation<T>> findInstanceMethods(Class<U> clazz, Class<T> annotationType) {
        return ReflectionUtils.getAllMethods(clazz)
                .stream()
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .flatMap(m -> Arrays.stream(m.getAnnotationsByType(annotationType)).map(a -> new MethodAndAnnotation<>(m, a)));
    }

}
