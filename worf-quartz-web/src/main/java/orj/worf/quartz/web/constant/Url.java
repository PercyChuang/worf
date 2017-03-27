package orj.worf.quartz.web.constant;

import orj.worf.web.base.constant.CommonUrl;
import orj.worf.web.base.tag.UrlConstant;

@UrlConstant(namespace = ProjectInfo.name)
public interface Url extends CommonUrl {
    String ROOT_PATH = "/task";
    String CREATE = METHOD_NEW;
    String LIST = "/list";
    String PAUSE = "/pause/{taskName}";
    String RESUME = "/resume/{taskName}";
    String TRIGGER = "/trigger/{taskName}";
    String DELETE = "/delete/{taskName}";
    String EDIT = "/edit/name/{taskName}/cron/{cronExpression}";
    String REDIRECT_TO_LIST = "redirect:" + ROOT_PATH + LIST;
    String FORWARD_TO_LIST = "forward:" + ROOT_PATH + LIST;
}
