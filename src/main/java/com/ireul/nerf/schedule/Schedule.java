package com.ireul.nerf.schedule;

import org.quartz.JobBuilder;
import org.quartz.utils.Key;

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
     * Job group name
     *
     * @return group name of this job
     */
    String group() default Key.DEFAULT_GROUP;

    /**
     * Job description
     *
     * @return description of this job
     */
    String desc() default "";

    /**
     * Should Job be re-executed if failed, this affects {@link JobBuilder#requestRecovery()}
     * <p><b>Most people should use {@link BaseJob#shouldRetry()}, {@link BaseJob#maxRetry()}, {@link BaseJob#retryDelay()}</b></p>
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
