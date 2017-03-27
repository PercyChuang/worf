package orj.worf.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class SpringPointcuts {
    @Pointcut("@within(org.springframework.stereotype.Repository)")
    public void springRepository() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void springService() {
    }

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void springController() {
    }

    @Pointcut("@within(org.springframework.stereotype.Component)")
    public void springComponent() {
    }

    @Pointcut("execution(@org.springframework.transaction.annotation.Transactional * * (..))")
    public void springTransactional() {
    }

    @Pointcut("springService() || springComponent()")
    public void springAnnotations() {
    }
}