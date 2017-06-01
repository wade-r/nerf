package com.ireul.nerf.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a field or a method as a injection source
 * <p>If a method is marked by {@link Inject}, it MUST NOT have parameters and MUST return a object. Marked method will
 * be invoked every time a field be injected.</p>
 *
 * @author Ryan Wade
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Provide {
}
