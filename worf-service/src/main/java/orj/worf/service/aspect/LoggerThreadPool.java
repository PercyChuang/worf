package orj.worf.service.aspect;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import orj.worf.util.ExecutorUtil;
import orj.worf.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggerThreadPool {
    private static final Logger logger = LoggerFactory.getLogger(LoggerThreadPool.class);

    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(PROCESSORS, PROCESSORS * 2, 15L,
            TimeUnit.MINUTES, new ArrayBlockingQueue(128), new NamedThreadFactory("Logger", true),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void log(LoggerStrategy loggerStrategy) {
        try {
            EXECUTOR.execute(loggerStrategy);
        } catch (Throwable cause) {
            logger.error(cause.getMessage());
        }
    }

    static {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    ExecutorUtil.gracefulShutdown(LoggerThreadPool.EXECUTOR, 1500);
                }
            });
        } catch (Throwable e) {
        }
    }
}
