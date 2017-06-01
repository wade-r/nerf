package com.ireul.nerf.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is for marking a command entry on application class
 *
 * @author Ryan Wade
 * @see com.ireul.nerf.application.Application
 * @see Command
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handle {
    /**
     * Command names can be handled by this method
     *
     * @return array of command names
     */
    String[] value();

    /**
     * Description of this method, will be printed by "help" command
     *
     * @return description
     */
    String desc() default "";
}
