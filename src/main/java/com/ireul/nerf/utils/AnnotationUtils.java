package com.ireul.nerf.utils;

import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by ryan on 5/30/17.
 */
public class AnnotationUtils {

    @FunctionalInterface
    public interface MethodAnnotationHandler<T> {

        void handle(Method method, T annotation);

    }

    /**
     * Find all instance methods annotated with specified annotation,
     * Will invoke handler multiple times if method is annotated with same annotation multiple times.
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
