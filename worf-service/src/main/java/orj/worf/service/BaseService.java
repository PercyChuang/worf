package orj.worf.service;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class BaseService {
    protected final XLogger logger;

    public BaseService() {
        this.logger = XLoggerFactory.getXLogger(super.getClass());
    }
}
