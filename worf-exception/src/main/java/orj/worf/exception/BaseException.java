package orj.worf.exception;

import java.util.Date;

public interface BaseException {

    String getCode();

    String[] getArgs();

    void setTime(Date time);

    Date getTime();

    void setClassName(String className);

    String getClassName();

    void setMethodName(String methodName);

    String getMethodName();

    void setParameters(String[] parameters);

    String[] getParameters();

    void setHandled(boolean handled);

    boolean isHandled();

    String getMessage();

    void setI18nMessage(String i18nMessage);

    String getI18nMessage();

}
