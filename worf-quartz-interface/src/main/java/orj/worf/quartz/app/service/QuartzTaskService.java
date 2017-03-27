package orj.worf.quartz.app.service;

import java.util.List;
import java.util.Map;

import orj.worf.quartz.app.dto.SchedulerJob;

public interface QuartzTaskService {

    /**
     * 获取所有任务.
     */
    Map<String, List<SchedulerJob>> getJobs() throws Exception;

    /**
     * 获取所有计划中的任务.
     */
    List<SchedulerJob> getPlanningJobs() throws Exception;

    /**
     * 获取所有执行中的任务.
     */
    List<SchedulerJob> getExecutingJobs() throws Exception;

    /**
     * 根据Name和Group添加或更新任务.
     */
    void saveOrUpdateJob(SchedulerJob job) throws Exception;

    /**
     * 暂停任务.
     */
    void pauseJob(SchedulerJob job) throws Exception;

    /**
     * 恢复任务.
     */
    void resumeJob(SchedulerJob job) throws Exception;

    /**
     * 触发执行一次任务.
     */
    void triggerJob(SchedulerJob job) throws Exception;

    /**
     * 删除任务.
     */
    void deleteJob(SchedulerJob job) throws Exception;

}
