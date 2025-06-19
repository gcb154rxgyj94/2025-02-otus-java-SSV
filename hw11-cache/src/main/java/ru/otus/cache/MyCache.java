package ru.otus.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Самописный кеш
 */
public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache;
    private final int maxSize;
    private final List<K> orderedKeysByAdd;
    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    public MyCache(int maxSize) {
        this.maxSize = maxSize;
        this.orderedKeysByAdd = new ArrayList<>(maxSize);
        this.cache = new WeakHashMap<>(maxSize);
    }

    @Override
    public void put(K key, V value) {
        if (cache.size() >= maxSize) {
            while (!orderedKeysByAdd.isEmpty()) {
                K key1 = orderedKeysByAdd.remove(0);
                if (cache.containsKey(key1)) {
                    cache.remove(key1);
                    break;
                }
            }
        }
        cache.put(key, value);
        orderedKeysByAdd.add(key);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        boolean exist = cache.containsKey(key);
        V value = cache.remove(key);
        if (exist) {
            orderedKeysByAdd.remove(key);
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
