package com.ireul.nerf.utils;

import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This is a helper class providing annotation related methods.
 *
 * @author Ryan Wade
 */
public class AnnotationUtils {

    /**
     * Functional interface for handling a Method / Annotation pair
     *
     * @param <T> Annotation type
     */
    @FunctionalInterface
    public interface MethodAnnotationHandler<T> {

        /**
         * Handle a Method / Annotation pair
         *
         * @param method     {@link Method} to handle
         * @param annotation {@link Annotation} to handle
         */
        void handle(Method method, T annotation);

    }

    /**
     * Iterate over all instance methods annotated with specified annotation.
     * <p>Handler will be invoked multiple times if method is annotated with same annotation multiple times.</p>
     *
     * @param clazz          class to find
     * @param annotationType annotation to find
     * @param handler        handle method / annotation combination
     * @param <T>            annotation type
     * @param <U>            class type
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation, U> void forEachInstanceMethod(Class<U> clazz, Class<T> annotationType, MethodAnnotationHandler<T> handler) {
        for (Method method : ReflectionUtils.getAllMethods(clazz)) {
            // skip static
            if (Modifier.isStatic(method.getModifiers())) continue;
            // find annotations
            T[] annotations = method.getAnnotationsByType(annotationType);
            if (annotations.length > 0) {
                // make accessible
                if (!method.isAccessible()) method.setAccessible(true);
                // execute annotations
                for (T annotation : annotations) {
                    handler.handle(method, annotation);
                }
            }
        }
    }

}
