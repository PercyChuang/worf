package orj.worf.tx.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class TxPointcuts {

    @Pointcut("@within(orj.worf.tx.annotation.TransactionalMark)")
    public void txMarkPointcut() {}
}