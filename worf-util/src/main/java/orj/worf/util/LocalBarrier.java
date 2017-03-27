package orj.worf.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class LocalBarrier {
    private static final ConcurrentMap<String, TimedCountDownLatch> barriers = new ConcurrentHashMap<String, TimedCountDownLatch>();
    private final String id;

    public LocalBarrier(final String id) {
        Assert.hasText(id);
        this.id = id;
    }

    public boolean setBarrier() {
        TimedCountDownLatch previous = barriers.putIfAbsent(this.id, new TimedCountDownLatch());
        return previous == null;
    }

    public synchronized boolean removeBarrier() {
        TimedCountDownLatch timedCountDownLatch = barriers.get(this.id);
        if (timedCountDownLatch == null) {
            return false;
        }
        CountDownLatch countDownLatch = timedCountDownLatch.getCountDownLatch();
        countDownLatch.countDown();
        barriers.remove(this.id);
        return true;
    }

    public boolean waitOnBarrier(final long maxWait, final TimeUnit unit) throws InterruptedException {
        TimedCountDownLatch timedCountDownLatch = barriers.get(this.id);
        if (timedCountDownLatch == null) {
            return false;
        }
        CountDownLatch countDownLatch = timedCountDownLatch.getCountDownLatch();
        return countDownLatch.await(maxWait, unit);
    }

    public synchronized void clearExpiredBarriers(final long timeout) {
        TimedCountDownLatch timedCountDownLatch = null;
        for (String key : barriers.keySet()) {
            timedCountDownLatch = barriers.get(key);
            if (System.currentTimeMillis() - timedCountDownLatch.getCreateTime() > timeout) {
                barriers.remove(key);
            }
        }
    }

    private class TimedCountDownLatch {
        private final long createTime;
        private final CountDownLatch countDownLatch;

        public TimedCountDownLatch() {
            this.createTime = System.currentTimeMillis();
            this.countDownLatch = new CountDownLatch(1);
        }

        public long getCreateTime() {
            return this.createTime;
        }

        public CountDownLatch getCountDownLatch() {
            return this.countDownLatch;
        }
    }
}