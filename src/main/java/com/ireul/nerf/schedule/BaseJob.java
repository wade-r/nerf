package com.ireul.nerf.schedule;

import com.ireul.nerf.application.Application;
import org.quartz.*;
import org.quartz.utils.Key;
import org.slf4j.Logger;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * This abstract class implements {@link Job} and provides injections
 * <p><b>Most people may use this class rather than implements {@link Job}</b></p>
 *
 * @author Ryan Wade
 */
public abstract class BaseJob implements Job {

    /**
     * Key in {@link JobExecutionContext#get(Object)} for injector
     */
    public static final String kINJECTOR = "__NERF_INJECTOR";

    /**
     * Key in {@link JobExecutionContext#getMergedJobDataMap()} for current retry count
     */
    public static final String kRETRY_COUNT = "__NERF_RETRY_COUNT";

    private JobExecutionContext executionContext;

    /**
     * Get the current {@link JobExecutionContext}
     *
     * @return context
     */
    public JobExecutionContext executionContext() {
        return this.executionContext;
    }

    /**
     * Get the current {@link Scheduler}
     *
     * @return scheduler
     */
    public Scheduler scheduler() {
        return executionContext().getScheduler();
    }

    /**
     * should retry if {@link #execute(JobDataMap)} throws something, subclass may override
     *
     * @return should retry, default to true
     */
    public boolean shouldRetry() {
        return true;
    }

    /**
     * max retry count of this job, subclass may override
     *
     * @return max retry count, default to 5
     */
    public int maxRetry() {
        return 5;
    }

    /**
     * delay in seconds before next retry, subclass may override
     *
     * @return delay in seconds, default to 1
     */
    public int retryDelay() {
        return 1;
    }

    private void retryIfNeeded() {
        // check shouldRetry()
        if (!shouldRetry()) return;

        // determine if max retry reached
        JobDataMap dataMap = executionContext().getMergedJobDataMap();
        int retryCount = 0;

        // if max retry exceeded just return
        if (dataMap.get(kRETRY_COUNT) != null) {
            retryCount = dataMap.getInt(kRETRY_COUNT);
            if (retryCount >= maxRetry()) {
                logger().error("Max retry exceeded for " + getClass().getCanonicalName());
                return;
            }
        }

        // update retryCount
        retryCount = retryCount + 1;
        dataMap.put(kRETRY_COUNT, retryCount);

        // reschedule
        try {
            JobDetail jobDetail = executionContext().getJobDetail();
            scheduler()
                    .scheduleJob(
                            newJob(getClass())
                                    .withIdentity(Key.createUniqueName(jobDetail.getKey().getGroup()))
                                    .usingJobData(dataMap)
                                    .withDescription(jobDetail.getDescription())
                                    .build(),
                            newTrigger()
                                    .startAt(new Date(System.currentTimeMillis() + retryDelay() * 1000))
                                    .build()
                    );
            logger().info("Retrying " + getClass().getCanonicalName());
        } catch (SchedulerException e) {
            logger().error("Failed to retry " + getClass().getCanonicalName(), e);
        }
    }

    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        // set executionContext
        this.executionContext = context;
        // inject
        Application application = (Application) context.get(kINJECTOR);
        application.injectTo(this);
        // execute
        try {
            execute(context.getMergedJobDataMap());
        } catch (Throwable throwable) {
            // log the error
            logger().error("Error occurred", throwable);
            // retry if needed
            retryIfNeeded();
        }
    }

    /**
     * Subclasses should implement this method, JobExecutionContext is already set and injections are done
     *
     * @throws Throwable any error occurred will be captured
     */
    public abstract void execute(JobDataMap dataMap) throws Exception;

    /**
     * Get the actual logger, should provide by subclass
     *
     * @return Logger
     */
    public abstract Logger logger();

}
