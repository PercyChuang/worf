package orj.worf.quartz.executor;

import orj.worf.core.util.ApplicationContextHolder;
import orj.worf.quartz.app.dto.Constants;
import orj.worf.quartz.app.dto.SchedulerJob;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJob.class);

    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        SchedulerJob schedulerJob = (SchedulerJob) jobDataMap.get(Constants.SCHEDULERJOB_KEY);
        String executorBeanId = (String) jobDataMap.get(Constants.JOBEXECUTOR_KEY);
        JobExecutor jobExecutor = ApplicationContextHolder.get().getBean(executorBeanId, JobExecutor.class);
        if (logger.isInfoEnabled()) {
            logger.info("定时任务[{}]开始执行", schedulerJob);
        }
        jobExecutor.execute();
        if (logger.isInfoEnabled()) {
            logger.info("定时任务[{}]执行完成", schedulerJob.getName());
        }
    }

}
