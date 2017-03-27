package orj.worf.util;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicPositiveLong extends Number implements Serializable {

    private static final long serialVersionUID = -8416688392313180434L;

    private final AtomicLong l;

    public AtomicPositiveLong() {
        l = new AtomicLong();
    }

    public AtomicPositiveLong(final long initialValue) {
        l = new AtomicLong(initialValue);
    }

    public final long getAndIncrement() {
        for (;;) {
            long current = l.get();
            long next = current >= Long.MAX_VALUE ? 0 : current + 1;
            if (l.compareAndSet(current, next)) {
                return current;
            }
        }
    }

    public final long getAndDecrement() {
        for (;;) {
            long current = l.get();
            long next = current <= 0 ? Long.MAX_VALUE : current - 1;
            if (l.compareAndSet(current, next)) {
                return current;
            }
        }
    }

    public final long incrementAndGet() {
        for (;;) {
            long current = l.get();
            long next = current >= Long.MAX_VALUE ? 0 : current + 1;
            if (l.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    public final long decrementAndGet() {
        for (;;) {
            long current = l.get();
            long next = current <= 0 ? Long.MAX_VALUE : current - 1;
            if (l.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    public final long get() {
        return l.get();
    }

    public final void set(final long newValue) {
        if (newValue < 0) {
            throw new IllegalArgumentException("new value " + newValue + " < 0");
        }
        l.set(newValue);
    }

    public final long getAndSet(final long newValue) {
        if (newValue < 0) {
            throw new IllegalArgumentException("new value " + newValue + " < 0");
        }
        return l.getAndSet(newValue);
    }

    public final long getAndAdd(final long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("delta " + delta + " < 0");
        }
        for (;;) {
            long current = l.get();
            long next = current >= Long.MAX_VALUE - delta + 1 ? delta - 1 : current + delta;
            if (l.compareAndSet(current, next)) {
                return current;
            }
        }
    }

    public final long addAndGet(final long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("delta " + delta + " < 0");
        }
        for (;;) {
            long current = l.get();
            long next = current >= Long.MAX_VALUE - delta + 1 ? delta - 1 : current + delta;
            if (l.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    public final boolean compareAndSet(final long expect, final long update) {
        if (update < 0) {
            throw new IllegalArgumentException("update value " + update + " < 0");
        }
        return l.compareAndSet(expect, update);
    }

    public final boolean weakCompareAndSet(final long expect, final long update) {
        if (update < 0) {
            throw new IllegalArgumentException("update value " + update + " < 0");
        }
        return l.weakCompareAndSet(expect, update);
    }

    @Override
    public byte byteValue() {
        return l.byteValue();
    }

    @Override
    public short shortValue() {
        return l.shortValue();
    }

    @Override
    public int intValue() {
        return l.intValue();
    }

    @Override
    public long longValue() {
        return l.longValue();
    }

    @Override
    public float floatValue() {
        return l.floatValue();
    }

    @Override
    public double doubleValue() {
        return l.doubleValue();
    }

    @Override
    public String toString() {
        return l.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (l == null ? 0 : l.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AtomicPositiveLong other = (AtomicPositiveLong) obj;
        if (l == null) {
            if (other.l != null) {
                return false;
            }
        } else if (!l.equals(other.l)) {
            return false;
        }
        return true;
    }

}