package orj.worf.quartz.app.dto;

import java.util.Date;

import orj.worf.core.model.BaseObject;
import orj.worf.util.DateUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class SchedulerJob extends BaseObject {

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
        this.name = name != null ? name.trim() : name;
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

    public String getNextFire() {
        if (nextFireTime == null) {
            return "";
        }
        return DateUtils.format("yyyy-MM-dd HH:mm:ss EEE", nextFireTime);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getBase64Name() {
        if (name == null) {
            return "";
        }
        return Base64.encodeBase64URLSafeString(name.getBytes());
    }

    public void setNameFromBase64(final String base64Name) {
        this.name = StringUtils.newStringUtf8(Base64.decodeBase64(base64Name));
    }

    public String getBase64CronExpression() {
        if (cronExpression == null) {
            return "";
        }
        return Base64.encodeBase64URLSafeString(cronExpression.getBytes());
    }

    public void setCronExpressionFromBase64(final String base64CronExpression) {
        this.cronExpression = StringUtils.newStringUtf8(Base64.decodeBase64(base64CronExpression));
    }

    public boolean isPaused() {
        return "PAUSED".equals(status);
    }

    public boolean isSuspendable() {
        return "NORMAL".equals(status) || "BLOCKED".equals(status);
    }

    public boolean isDeletable() {
        return "PAUSED".equals(status) || "NORMAL".equals(status) || "BLOCKED".equals(status);
    }

    public boolean isExecutable() {
        return "PAUSED".equals(status) || "NORMAL".equals(status) || "BLOCKED".equals(status);
    }

    public boolean isTempTrigger() {
        return cronExpression == null || cronExpression.isEmpty();
    }

}
