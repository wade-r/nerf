package com.ireul.nerf.command;

import java.lang.annotation.*;

/**
 * This annotation is for marking a command entry on application class
 *
 * @author Ryan Wade
 * @see com.ireul.nerf.application.Application
 * @see Command
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Handles.class)
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

    /**
     * Priority of this method, lesser will be execute earlier, default is 0
     *
     * @return priority of this method
     */
    int priority() default 0;
}
