package orj.worf.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LRUCache<K, V> implements Serializable {
    private static final long serialVersionUID = 583514357218318596L;
    private final int size;
    private final long maxAge;
    private final LinkedHashMap<K, ManagedItem<V>> map;

    public LRUCache(final int size) {
        this(size, -1L);
    }

    public LRUCache(final int size, final long maxAge) {
        this.size = size;
        this.maxAge = maxAge;
        this.map = new LinkedHashMap<K, ManagedItem<V>>(size, 0.75F, true);
    }

    public V put(final K key, final V value) {
        synchronized (map) {
            ManagedItem<V> previous = this.map.put(key, new ManagedItem<V>(value, System.currentTimeMillis()));
            while (this.map.size() > this.size) {
                this.map.remove(this.map.keySet().iterator().next());
            }
            return previous != null ? previous.value : null;
        }
    }

    public V putIfDifferent(final K key, final V value) {
        synchronized (map) {
            ManagedItem<V> item = this.map.get(key);
            if (item == null || item.value == null || !item.value.equals(value)) {
                return put(key, value);
            }
            return item.value;
        }
    }

    public V get(final K key) {
        synchronized (map) {
            ManagedItem<V> item = this.map.get(key);
            if (item == null) {
                return null;
            }
            if (this.maxAge > -1L && System.currentTimeMillis() - item.insertionTime > this.maxAge) {
                this.map.remove(key);
                return null;
            }
            return item.value;
        }
    }

    public V remove(final K key) {
        synchronized (map) {
            ManagedItem<V> removed = this.map.remove(key);
            return removed != null ? removed.value : null;
        }
    }

    public boolean containsKey(final K key) {
        synchronized (map) {
            return this.map.containsKey(key);
        }
    }

    public List<K> getKeys() {
        synchronized (map) {
            return new ArrayList<K>(this.map.keySet());
        }
    }

    private static final class ManagedItem<T> implements Serializable {
        private static final long serialVersionUID = 1056054693015848154L;
        private final T value;
        private final long insertionTime;

        private ManagedItem(final T value, final long accessTime) {
            this.value = value;
            this.insertionTime = accessTime;
        }
    }

}