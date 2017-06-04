package com.ireul.nerf.schedule;

import com.ireul.nerf.application.Application;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This abstract class implements {@link Job} and provides injections
 * <p><b>Most people may use this class rather than implements {@link Job}</b></p>
 *
 * @author Ryan Wade
 */
public abstract class BaseJob implements Job {

    private JobExecutionContext executionContext;

    public JobExecutionContext executionContext() {
        return this.executionContext;
    }

    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        // set executionContext
        this.executionContext = context;
        // inject
        Application application = (Application) context.get(ScheduleContext.INJECTOR_KEY);
        application.injectTo(this);
        // execute
        execute();
    }

    /**
     * Subclasses should implement this method, JobExecutionContext is already set and injections are done
     *
     * @throws JobExecutionException job execution exception from {@link org.quartz}
     */
    public abstract void execute() throws JobExecutionException;

}
