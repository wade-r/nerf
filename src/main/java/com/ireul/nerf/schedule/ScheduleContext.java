package com.ireul.nerf.schedule;

import com.ireul.nerf.application.Application;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author Ryan Wade
 */
public class ScheduleContext {

    private final Logger logger = LoggerFactory.getLogger(ScheduleContext.class);

    private Application application;

    private Scheduler scheduler;

    public ScheduleContext(Application application) {
        this.application = application;
    }

    public void setup(HashMap<String, String> options) {
        String configFile = options.get("quartz-config");
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
        // scan and schedule
        ScheduleUtils.findJobs(this.application.getClass()).forEach(caa -> {
            Schedule a = caa.annotation;
            JobDetail job = newJob(caa.classType)
                    .requestRecovery(a.recovery())
                    .withIdentity(a.name(), a.group())
                    .build();
            Trigger trigger = null;
            if (a.interval() > 0) {
                trigger = newTrigger()
                        .startNow()
                        .withSchedule(simpleSchedule().withIntervalInSeconds((int) a.interval()).repeatForever())
                        .build();
            } else if (a.cron().length() > 0) {
                trigger = newTrigger()
                        .startNow()
                        .withSchedule(cronSchedule(a.cron()))
                        .build();
            } else {
                logger.error("No interval() and cron() found from @Schedule on class " + caa.classType.getName());
            }
            if (trigger != null) {
                try {
                    this.scheduler.scheduleJob(job, trigger);
                } catch (SchedulerException e) {
                    logger.error("Cannot add Job for " + caa.classType.getName(), e);
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

}
