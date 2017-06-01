package com.ireul.nerf.inject;

import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This is a virtual interface, it provides {@link #injectTo(Object)} method to any class
 * <p>Any class implements this interface can become an injection source.</p>
 * <p>For example:</p>
 * <pre>
 *     class TargetClass  {
 *         \@Inject
 *         String targetValue;
 *     }
 *
 *     class SourceClass {
 *         \@Provide
 *         String sourceValue = "hello";
 *
 *         void method() {
 *             TargetClass target = new TargetClass();
 *             this.injectTo(target);
 *             target.targetValue.equals(this.sourceValue);
 *         }
 *     }
 * </pre>
 *
 * @author Ryan Wade
 * @see Inject
 * @see Provide
 */
public interface Injector {

    /**
     * Search own fields and methods marked with {@link Provide} and inject them to target object
     *
     * @param target injection target object
     */
    // TODO: 2017/6/1 optimize performance
    @SuppressWarnings("unchecked")
    default <T> void injectTo(T target) {
        ReflectionUtils.getAllFields(target.getClass()).forEach(f -> {
            // skip static
            if (Modifier.isStatic(f.getModifiers())) return;
            // check @Inject
            if (f.getAnnotation(Inject.class) == null) return;
            // make it accessible
            if (!f.isAccessible()) f.setAccessible(true);

            // injected flag
            boolean injected = false;

            // search fields
            for (Field lf : ReflectionUtils.getAllFields(this.getClass())) {
                // check @Provide
                if (lf.getAnnotation(Provide.class) == null) continue;
                // check assignable
                if (!f.getType().isAssignableFrom(lf.getType())) continue;
                // make it accessible
                if (!lf.isAccessible()) lf.setAccessible(true);
                // assign
                try {
                    f.set(target, lf.get(this));
                    injected = true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            if (injected) return;

            // search methods
            for (Method m : ReflectionUtils.getAllMethods(this.getClass())) {
                // check @Provide
                if (m.getAnnotation(Provide.class) == null) continue;
                // check assignable
                if (!f.getType().isAssignableFrom(m.getReturnType())) continue;
                // make it accessible
                if (!m.isAccessible()) m.setAccessible(true);
                // invoke and assign
                try {
                    f.set(target, m.invoke(this));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
