package orj.worf.quartz.model;

import java.io.Serializable;
import java.util.Date;

import orj.worf.quartz.constant.Constants;

public class SchedulerJob implements Serializable {

    private static final long serialVersionUID = -8272307355930247942L;

    /**
     * 任务处理类BeanId
     */
    private String beanId;

    /**
     * 任务名
     */
    private String name;

    /**
     * 任务组
     */
    private String group = Constants.GROUP_NAME;

    /**
     * 表达式
     */
    private String cronExpression;
    /**
     * 状态
     */
    private String status;

    /**
     * 描述
     */
    private String description;

    /**
     * 下一次执行时间
     */
    private Date nextFireTime;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(final String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(final String beanId) {
        this.beanId = beanId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(final Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SchedulerJob [name=");
        builder.append(name);
        builder.append(", cronExpression=");
        builder.append(cronExpression);
        builder.append(", status=");
        builder.append(status);
        builder.append(", desc=");
        builder.append(description);
        builder.append(", nextFireTime=");
        builder.append(nextFireTime);
        builder.append("]");
        return builder.toString();
    }

}
