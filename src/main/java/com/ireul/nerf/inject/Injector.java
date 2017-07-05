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
    default void injectTo(Object target) {
        ReflectionUtils.getAllFields(target.getClass()).forEach(targetField -> {
            // skip static
            if (Modifier.isStatic(targetField.getModifiers())) return;
            // check @Inject
            if (targetField.getAnnotation(Inject.class) == null) return;
            // make it accessible
            if (!targetField.isAccessible()) targetField.setAccessible(true);

            // injected flag
            boolean injected = false;

            // search fields
            for (Field localField : ReflectionUtils.getAllFields(this.getClass())) {
                // check @Provide
                if (localField.getAnnotation(Provide.class) == null) continue;
                // check assignable
                if (!targetField.getType().isAssignableFrom(localField.getType())) continue;
                // make it accessible
                if (!localField.isAccessible()) localField.setAccessible(true);
                // assign
                try {
                    // value injected
                    targetField.set(target, localField.get(this));
                    injected = true;
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            if (injected) return;

            // search methods
            for (Method localMethod : ReflectionUtils.getAllMethods(this.getClass())) {
                // check @Provide
                if (localMethod.getAnnotation(Provide.class) == null) continue;
                // check assignable
                if (!targetField.getType().isAssignableFrom(localMethod.getReturnType())) continue;
                // make it accessible
                if (!localMethod.isAccessible()) localMethod.setAccessible(true);
                // invoke and assign
                try {
                    // value injected
                    targetField.set(target, localMethod.invoke(this));
                    break;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
