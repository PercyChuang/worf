package orj.worf.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import orj.worf.core.util.ApplicationContextHolder;
import orj.worf.quartz.app.dto.Constants;
import orj.worf.quartz.app.dto.SchedulerJob;
import orj.worf.quartz.app.service.QuartzTaskService;
import orj.worf.quartz.executor.JobExecutor;
import orj.worf.quartz.executor.QuartzJob;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

public class SchedulerService implements QuartzTaskService {

    private Scheduler scheduler;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<SchedulerJob>> getJobs() throws Exception {
        Map<String, List<SchedulerJob>> map = new HashMap<String, List<SchedulerJob>>();
        map.put(Constants.PLANNINGS, getPlanningJobs());
        map.put(Constants.EXECUTINGS, getExecutingJobs());
        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SchedulerJob> getPlanningJobs() throws Exception {
        List<SchedulerJob> jobList = new ArrayList<SchedulerJob>();
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Constants.GROUP_NAME));
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                SchedulerJob job = new SchedulerJob();
                job.setName(jobKey.getName());
                job.setDescription(trigger.getDescription());
                TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setStatus(triggerState.name());
                job.setNextFireTime(trigger.getNextFireTime());
                job.setBeanId(String.valueOf(scheduler.getJobDetail(jobKey).getJobDataMap()
                        .get(Constants.JOBEXECUTOR_KEY)));
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    job.setCronExpression(cronTrigger.getCronExpression());
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SchedulerJob> getExecutingJobs() throws Exception {
        List<SchedulerJob> jobList = new ArrayList<SchedulerJob>();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        for (JobExecutionContext executingJob : executingJobs) {
            SchedulerJob job = new SchedulerJob();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setName(jobKey.getName());
            job.setDescription(trigger.getDescription());
            TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setStatus(triggerState.name());
            job.setNextFireTime(trigger.getNextFireTime());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdateJob(final SchedulerJob job) throws Exception {
        TriggerKey triggerKey = new TriggerKey(job.getName(), job.getGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            ApplicationContextHolder.get().getBean(job.getBeanId(), JobExecutor.class);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withDescription(job.getDescription());
            trigger = triggerBuilder.withIdentity(job.getName(), job.getGroup()).withSchedule(scheduleBuilder).build();
            JobBuilder jobBuilder = JobBuilder.newJob(QuartzJob.class).withDescription(job.getDescription());
            JobDetail jobDetail = jobBuilder.withIdentity(job.getName(), job.getGroup()).build();
            jobDetail.getJobDataMap().put(Constants.SCHEDULERJOB_KEY, job);
            jobDetail.getJobDataMap().put(Constants.JOBEXECUTOR_KEY, job.getBeanId());
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pauseJob(final SchedulerJob job) throws Exception {
        JobKey jobKey = JobKey.jobKey(job.getName(), job.getGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resumeJob(final SchedulerJob job) throws Exception {
        JobKey jobKey = JobKey.jobKey(job.getName(), job.getGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerJob(final SchedulerJob job) throws Exception {
        JobKey jobKey = JobKey.jobKey(job.getName(), job.getGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteJob(final SchedulerJob job) throws Exception {
        JobKey jobKey = JobKey.jobKey(job.getName(), job.getGroup());
        scheduler.deleteJob(jobKey);
    }

    public void setScheduler(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void close() throws SchedulerException {
        this.scheduler.shutdown();
    }

}
