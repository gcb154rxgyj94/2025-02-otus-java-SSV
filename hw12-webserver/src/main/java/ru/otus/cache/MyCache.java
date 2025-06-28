package ru.otus.cache;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Кеш
 */
public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        boolean exist = cache.containsKey(key);
        V value = cache.remove(key);
        if (exist) {
            notifyListeners(key, value, "remove");
        }
    }

    @Override
    public V get(K key) {
        boolean exist = cache.containsKey(key);
        V val = cache.get(key);
        if (exist) {
            notifyListeners(key, val, "get");
        }
        return val;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
