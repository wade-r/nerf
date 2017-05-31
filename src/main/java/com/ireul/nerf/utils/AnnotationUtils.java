package com.ireul.nerf.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Created by ryan on 5/30/17.
 */
public class AnnotationUtils {

    @FunctionalInterface
    public interface MethodAnnotationHandler<T> {

        void handle(Method method, T annotation);

    }

    public static <T extends Annotation> void findInstanceMethod(Object object, Class<T> annotationType, MethodAnnotationHandler<T> handler) {
        findInstanceMethod(object.getClass(), annotationType, handler);
    }

    public static <T extends Annotation, U> void findInstanceMethod(Class<U> clazz, Class<T> annotationType, MethodAnnotationHandler<T> handler) {
        Arrays.stream(clazz.getMethods())
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .forEach(method -> {
                    T[] annotations = method.getAnnotationsByType(annotationType);
                    for (T annotation : annotations) {
                        handler.handle(method, annotation);
                    }
                });
    }

}
