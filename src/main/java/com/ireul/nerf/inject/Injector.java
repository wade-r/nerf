package com.ireul.nerf.inject;

import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Injector is a virtual interface, it read own fields and methods and
 * Created by ryan on 5/27/17.
 */
public interface Injector {

    /**
     * read own fields and methods marked with Provide
     *
     * @param target
     */
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
