package orj.worf.util;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicPositiveInteger POOL_SEQ = new AtomicPositiveInteger(1);
    private final AtomicPositiveInteger threadNumber = new AtomicPositiveInteger(1);
    private final ThreadGroup threadGroup;
    private final String namePrefix;
    private final boolean isDaemon;

    public NamedThreadFactory() {
        this("Thread-Pool" + POOL_SEQ.getAndIncrement());
    }

    public NamedThreadFactory(final String name) {
        this(name, false);
    }

    public NamedThreadFactory(final String preffix, final boolean daemon) {
        namePrefix = preffix + "-thread-";
        isDaemon = daemon;
        SecurityManager s = System.getSecurityManager();
        threadGroup = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        String name = namePrefix + threadNumber.getAndIncrement();
        Thread thread = new Thread(threadGroup, runnable, name, 0);
        thread.setDaemon(isDaemon);
        return thread;
    }

}
