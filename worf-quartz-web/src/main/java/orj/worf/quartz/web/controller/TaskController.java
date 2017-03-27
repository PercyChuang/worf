package orj.worf.quartz.web.controller;

import java.util.List;
import java.util.Map;

import orj.worf.quartz.app.dto.Constants;
import orj.worf.quartz.app.dto.SchedulerJob;
import orj.worf.quartz.web.constant.Url;
import orj.worf.quartz.web.constant.Views;
import orj.worf.quartz.web.service.SchedulerService;
import orj.worf.web.base.controller.BaseController;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = Url.ROOT_PATH)
public class TaskController extends BaseController {

    @Autowired
    private SchedulerService schedulerService;

    @RequestMapping(value = Url.METHOD_INDEX, method = RequestMethod.GET)
    public String index() {
        return Url.FORWARD_TO_LIST;
    }

    @RequestMapping(value = Url.LIST, method = RequestMethod.GET)
    public String list(final ModelMap modelMap) {
        Map<String, List<SchedulerJob>> jobs = schedulerService.getJobs();
        modelMap.addAttribute(Constants.PLANNINGS, jobs.get(Constants.PLANNINGS));
        modelMap.addAttribute(Constants.EXECUTINGS, jobs.get(Constants.EXECUTINGS));
        return Views.LIST_VIEW;
    }

    @RequestMapping(value = Url.CREATE, method = RequestMethod.GET)
    public String newTask() {
        return Views.NEW_TASK_FORM;
    }

    @RequestMapping(value = Url.CREATE, method = RequestMethod.POST)
    public String newTask(final SchedulerJob schedulerJob) {
        validateSchedulerJob(schedulerJob, true);
        schedulerService.saveOrUpdateJob(schedulerJob);
        return Url.REDIRECT_TO_LIST;
    }

    @RequestMapping(value = Url.PAUSE)
    public String pause(@PathVariable final String taskName) {
        validateTaskName(taskName);
        SchedulerJob schedulerJob = new SchedulerJob();
        schedulerJob.setNameFromBase64(taskName);
        schedulerService.pauseJob(schedulerJob);
        return Url.REDIRECT_TO_LIST;
    }

    @RequestMapping(value = Url.RESUME)
    public String resume(@PathVariable final String taskName) {
        validateTaskName(taskName);
        SchedulerJob schedulerJob = new SchedulerJob();
        schedulerJob.setNameFromBase64(taskName);
        schedulerService.resumeJob(schedulerJob);
        return Url.REDIRECT_TO_LIST;
    }

    @RequestMapping(value = Url.DELETE)
    public String delete(@PathVariable final String taskName) {
        validateTaskName(taskName);
        SchedulerJob schedulerJob = new SchedulerJob();
        schedulerJob.setNameFromBase64(taskName);
        schedulerService.deleteJob(schedulerJob);
        return Url.REDIRECT_TO_LIST;
    }

    @RequestMapping(value = Url.TRIGGER)
    public String trigger(@PathVariable final String taskName) {
        validateTaskName(taskName);
        SchedulerJob schedulerJob = new SchedulerJob();
        schedulerJob.setNameFromBase64(taskName);
        schedulerService.triggerJob(schedulerJob);
        return Url.REDIRECT_TO_LIST;
    }

    @RequestMapping(value = Url.EDIT, method = RequestMethod.GET)
    public String edit(@PathVariable final String taskName, @PathVariable final String cronExpression,
            final ModelMap modelMap) {
        validateTaskName(taskName);
        SchedulerJob schedulerJob = new SchedulerJob();
        schedulerJob.setNameFromBase64(taskName);
        schedulerJob.setCronExpressionFromBase64(cronExpression);
        modelMap.addAttribute("taskName", schedulerJob.getName());
        modelMap.addAttribute("olderCronExpression", schedulerJob.getCronExpression());
        return Views.EDIT_TASK_FORM;
    }

    @RequestMapping(value = Url.METHOD_EDIT, method = RequestMethod.POST)
    public String edit(final SchedulerJob schedulerJob) {
        validateSchedulerJob(schedulerJob, false);
        schedulerService.saveOrUpdateJob(schedulerJob);
        return Url.REDIRECT_TO_LIST;
    }

    private void validateTaskName(final String taskName) {
        if (StringUtils.isBlank(taskName)) {
            throw new IllegalArgumentException("Task name is blank.");
        }
    }

    private void validateSchedulerJob(final SchedulerJob schedulerJob, final boolean isNew) {
        if (schedulerJob == null) {
            throw new IllegalArgumentException("Scheduler Job is null.");
        }
        if (isNew && StringUtils.isBlank(schedulerJob.getBeanId())) {
            throw new IllegalArgumentException("BeanId is blank.");
        }
        if (StringUtils.isBlank(schedulerJob.getName())) {
            throw new IllegalArgumentException("Name is blank.");
        }
        if (StringUtils.isBlank(schedulerJob.getCronExpression())) {
            throw new IllegalArgumentException("CronExpression is blank.");
        }
    }
}
