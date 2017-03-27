package orj.worf.tx.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class DataSourcePointcuts {

    @Pointcut("@within(orj.worf.tx.annotation.DataSource)")
    public void dsMarkPointcut() {}
}