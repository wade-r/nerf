package com.ireul.nerf.schedule;

import java.lang.annotation.*;

/**
 * This annotation represents a Quartz Trigger configuration
 * <b>ONE and ONLY ONE of cron() or interval() should be used</b>
 *
 * @author Ryan Wade
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Schedules.class)
public @interface Schedule {
    /**
     * Job name
     *
     * @return name of this job
     */
    String name() default "";

    /**
     * Job group name
     *
     * @return group name of this job
     */
    String group() default "";

    /**
     * Job description
     *
     * @return description of this job
     */
    String desc() default "";

    /**
     * Should Job be re-executed if failed
     *
     * @return should recovery
     */
    boolean recovery() default false;

    /**
     * Fire interval
     *
     * @return interval in seconds
     */
    long interval() default 0;

    /**
     * Delay before first start by interval()
     *
     * @return delay in seconds
     */
    long delay() default 0;

    /**
     * Cron expression
     *
     * @return cron expression
     */
    String cron() default "";
}
