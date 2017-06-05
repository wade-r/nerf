package com.ireul.nerf.utils;

import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

/**
 * Created by ryan on 6/1/17.
 */
public class AnnotationUtilsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface TargetAnnotations {
        TargetAnnotation[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Repeatable(TargetAnnotations.class)
    private @interface TargetAnnotation {
    }

    private static class TargetClass {

        @TargetAnnotation
        static void targetStaticMethod() {
        }

        @TargetAnnotation
        void targetMethod1() {
        }

        @TargetAnnotation
        @TargetAnnotation
        @TargetAnnotation
        void targetMethod2() {
        }

    }

    @Test
    public void forEachInstanceMethod() throws Exception {
        final int[] counter = {0};
        final int[] method1Counter = {0};
        final int[] method2Counter = {0};
        AnnotationUtils.findInstanceMethods(TargetClass.class, TargetAnnotation.class).forEach(maa -> {
            Method m = maa.method;
            TargetAnnotation a = maa.annotation;
            counter[0]++;
            if (m.getName().equalsIgnoreCase("targetMethod1")) {
                method1Counter[0]++;
            }
            if (m.getName().equalsIgnoreCase("targetMethod2")) {
                method2Counter[0]++;
            }
        });
        assertTrue(counter[0] == 4);
        assertTrue(method1Counter[0] == 1);
        assertTrue(method2Counter[0] == 3);
    }

}