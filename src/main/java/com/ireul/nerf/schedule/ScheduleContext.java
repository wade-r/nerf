package com.ireul.nerf.schedule;

import com.ireul.nerf.application.Application;
import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.utils.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Provides {@link org.quartz} wrappers
 * <p><b>Most people may subclass {@link BaseJob} rather than implements {@link Job}</b></p>
 *
 * @author Ryan Wade
 */
public class ScheduleContext {

    private static final String OPTION_QUARTZ_CONFIG = "quartz-config";

    private final Logger logger = LoggerFactory.getLogger(ScheduleContext.class);

    private Application application;

    private Scheduler scheduler;

    public ScheduleContext(Application application) {
        this.application = application;
    }

    public void setup(HashMap<String, String> options) {
        String configFile = options.get(OPTION_QUARTZ_CONFIG);
        // setup scheduler
        if (configFile == null) {
            try {
                DirectSchedulerFactory.getInstance().createVolatileScheduler(10);
                this.scheduler = DirectSchedulerFactory.getInstance().getScheduler();
            } catch (SchedulerException e) {
                logger.error("Cannot create simple scheduler", e);
                System.exit(1);
            }
        } else {
            try {
                this.scheduler = new StdSchedulerFactory(configFile).getScheduler();
            } catch (SchedulerException e) {
                logger.error("Cannot create standard scheduler", e);
                System.exit(1);
            }
        }
        // add InjectionListener
        try {
            this.scheduler.getListenerManager().addJobListener(new InjectionListener());
        } catch (SchedulerException e) {
            logger.error("Cannot add InjectionListener to scheduler");
            System.exit(1);
        }
        // scan and schedule
        ScheduleUtils.findJobs(this.application.getClass()).forEach(caa -> {
            Schedule schedule = caa.getAnnotation();
            JobDetail job = newJob(caa.getClassType())
                    .withIdentity(Key.createUniqueName(schedule.group()))
                    .requestRecovery(schedule.recovery())
                    .withDescription(schedule.desc())
                    .build();
            Trigger trigger = null;
            if (schedule.interval() > 0) {
                if (schedule.cron().length() > 0) {
                    logger.error(
                            "Both interval() and cron() found from @Schedule, schedule() will be used: "
                                    + caa.getClassType().getCanonicalName()
                    );
                }
                trigger = newTrigger()
                        .startAt(new Date(System.currentTimeMillis() + schedule.delay() * 1000))
                        .withSchedule(
                                simpleSchedule()
                                        .withIntervalInSeconds((int) schedule.interval())
                                        .repeatForever()
                        )
                        .build();
            } else if (schedule.cron().length() > 0) {
                trigger = newTrigger()
                        .startNow()
                        .withSchedule(cronSchedule(schedule.cron()))
                        .build();
            } else {
                logger.error(
                        "No interval() and cron() found from @Schedule on class "
                                + caa.getClassType().getCanonicalName()
                );
            }
            if (trigger != null) {
                try {
                    this.scheduler.scheduleJob(job, trigger);
                } catch (SchedulerException e) {
                    logger.error("Cannot add Job for " + caa.getClassType().getCanonicalName(), e);
                }
            }
        });
    }

    public void start() {
        try {
            this.scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        if (this.scheduler != null) {
            try {
                this.scheduler.shutdown();
            } catch (SchedulerException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public Application application() {
        return this.application;
    }

    private class InjectionListener implements JobListener {

        @Override
        public String getName() {
            return "InjectionListener";
        }

        @Override
        public void jobToBeExecuted(JobExecutionContext context) {
            context.put(BaseJob.kINJECTOR, application());
        }

        @Override
        public void jobExecutionVetoed(JobExecutionContext context) {
        }

        @Override
        public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        }
    }

}
