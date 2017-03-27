package orj.worf.quartz.web.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orj.worf.core.service.BaseService;
import orj.worf.quartz.app.dto.Constants;
import orj.worf.quartz.app.dto.SchedulerJob;
import orj.worf.quartz.app.service.QuartzTaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService extends BaseService {

    @Autowired
    private QuartzTaskService taskService;

    public Map<String, List<SchedulerJob>> getJobs() {
        try {
            return taskService.getJobs();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Map<String, List<SchedulerJob>> map = new HashMap<String, List<SchedulerJob>>();
            map.put(Constants.PLANNINGS, new ArrayList<SchedulerJob>(0));
            map.put(Constants.PLANNINGS, new ArrayList<SchedulerJob>(0));
            return map;
        }
    }

    /**
     * 获取所有计划中的任务.
     */
    public List<SchedulerJob> getPlanningJobs() {
        try {
            return taskService.getPlanningJobs();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    /**
     * 获取所有执行中的任务.
     */
    public List<SchedulerJob> getExecutingJobs() {
        try {
            return taskService.getExecutingJobs();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    /**
     * 根据Name和Group添加或更新任务.
     */
    public void saveOrUpdateJob(final SchedulerJob job) {
        try {
            taskService.saveOrUpdateJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 暂停任务.
     */
    public void pauseJob(final SchedulerJob job) {
        try {
            taskService.pauseJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 恢复任务.
     */
    public void resumeJob(final SchedulerJob job) {
        try {
            taskService.resumeJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 触发执行一次任务.
     */
    public void triggerJob(final SchedulerJob job) {
        try {
            taskService.triggerJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 删除任务.
     */
    public void deleteJob(final SchedulerJob job) {
        try {
            taskService.deleteJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
