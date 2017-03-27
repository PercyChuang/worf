package orj.worf.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class SystemTimer {

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private static final long tickUnit = Long.parseLong(System.getProperty("systemTimer.tick", "50"));
    private static volatile long timeMillis = System.currentTimeMillis();

    static {
        EXECUTOR.scheduleAtFixedRate(new TimerTicker(), tickUnit, tickUnit, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                EXECUTOR.shutdown();
            }
        });
    }

    private SystemTimer() {
    }

    public static long currentTimeMillis() {
        return timeMillis;
    }

    private static class TimerTicker implements Runnable {
        @Override
        public void run() {
            timeMillis = System.currentTimeMillis();
        }
    }
}
