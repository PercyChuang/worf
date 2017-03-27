package orj.worf.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Defines a simple cache to hold object instances by key. DataCache uses SoftReferences to keep track of items - these
 * could be lost during garbage collection scan. <P> DataCache can also be given a maxAge, if defined then any items
 * OLDER than maxAge are swept from the cache.
 */
public final class DataCache<K, V> {

    private final ConcurrentMap<K, Entry<K, V>> cachedData = new ConcurrentHashMap<K, Entry<K, V>>();

    private final ReferenceQueue<V> queue = new ReferenceQueue<V>();

    private final ReentrantLock lock = new ReentrantLock();

    private String name;
    /**
     * Max age of an entry in cache - if maxAge <=0 there is no timeout
     */
    private long maxAge;

    private long nextReapTime = 0;

    /**
     * Map contains an entry which refers to the key and a SoftReference. <p> We extend SoftReference so reference queue
     * handler gives us Entry and we can get the key again. <p>Making it extend SoftReference cuts out one object per
     * entry
     */
    private static class Entry<K, V> extends SoftReference<V> {
        final K key;
        final long expiry;

        private Entry(final K key, final V value, final ReferenceQueue<V> queue, final long maxTime) {
            super(value, queue);
            this.key = key;
            this.expiry = maxTime;
        }

        boolean hasExpired() {
            return expiry > 0 && System.currentTimeMillis() >= expiry;
        }
    }

    /**
     * Create a data cache to hold key-value associations.
     */
    public DataCache(final String name) {
        this(name, 0);
    }

    /**
     * Create a data cache to hold key-value associations.
     *
     * @param maxAge The longest time in milliseconds that this entry will be allowed to reach before being returned
     *        from the cache (0= unlimited)
     */
    public DataCache(final String name, final long maxAge) {
        this.maxAge = maxAge > 0 ? maxAge : 0;
        this.name = name;
    }

    /**
     * Find an object in the cache. <p> The cache is free to discard the reference to the object at any time, even
     * during the retrieval.
     *
     * @param key The key used to store the object last time
     * @return The cached object or null if not found/expired
     */
    public V get(final Object key) {
        V value = null;
        if (key != null) {
            lock.lock();
            try {
                flushEntries();
                Entry<K, V> entry = cachedData.get(key);
                if (entry != null) {
                    if (entry.hasExpired()) {
                        discard(entry);
                    } else {
                        value = entry.get();
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return value;
    }

    /**
     * Save or replace an object in the cache.
     *
     * @param key The key used to store the object last time
     * @param value The object to save.
     */
    public void put(final K key, final V value) {
        put(key, value, maxAge);
    }

    /**
     * Save or replace an object in the cache.
     *
     * @param key The key used to store the object last time
     * @param value The object to save.
     */
    private void put(final K key, final V value, final long maxage) {
        if (key != null) {
            lock.lock();
            try {
                if (value != null) {
                    final long now = System.currentTimeMillis();
                    final long max = maxage > 0 ? now + maxage : 0;
                    final Entry<K, V> entry = new Entry<K, V>(key, value, queue, max);
                    if (max > 0 && nextReapTime == 0) {
                        nextReapTime = max;
                    }
                    cachedData.put(key, entry);
                } else {
                    discard(key);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Removes a key from the cache.
     */
    private void discard(final Object key) {
        Entry<K, V> oldEntry = cachedData.get(key);
        if (oldEntry != null) {
            discard(oldEntry);
        }
    }

    /**
     * Removes an entry from the cache ONLY if it is still current in the cache. <p> If the entry is not linked in the
     * cache, there is nothing to do.
     */
    private void discard(final Entry<K, V> entry) {
        Entry<K, V> curr = cachedData.get(entry.key);
        if (curr == entry) {
            cachedData.remove(entry.key);
        }
    }

    /**
     * Checks reference queue and kills off entries which are not holding any data.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void flushEntries() {
        Entry<K, V> entry = null;
        while ((entry = (Entry) queue.poll()) != null) {
            discard(entry);
        }
        final long now = System.currentTimeMillis();
        if (maxAge > 0 && nextReapTime > 0 && nextReapTime < now) {
            for (Entry<K, V> e : cachedData.values()) {
                if (e.hasExpired()) {
                    discard(e);
                }
            }
            nextReapTime = cachedData.size() > 0 ? System.currentTimeMillis() + maxAge : 0;
        }
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     */
    public Set<K> keySet() {
        lock.lock();
        try {
            flushEntries();
            return cachedData.keySet();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes the mapping for a key from this map if it is present (optional operation). More formally, if this map
     * contains a mapping from key <tt>k</tt> to value <tt>v</tt> such that <code>(key==null ? k==null :
     * key.equals(k))</code>, that mapping is removed. (The map can contain at most one such mapping.)
     *
     * <p>Returns the value to which this map previously associated the key, or <tt>null</tt> if the map contained no
     * mapping for the key.
     */
    public V remove(final Object key) {
        V value = null;
        lock.lock();
        try {
            flushEntries();
            Entry<K, V> entry = cachedData.remove(key);
            if (entry != null) {
                if (entry.hasExpired()) {
                    discard(entry);
                } else {
                    value = entry.get();
                }
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Discard everything from the cache.
     */
    public void clear() {
        lock.lock();
        try {
            flushEntries();
            cachedData.clear();
            nextReapTime = 0;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            flushEntries();
            return cachedData.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "DataCache[" + name + " sz=" + size() + ']';
    }
}