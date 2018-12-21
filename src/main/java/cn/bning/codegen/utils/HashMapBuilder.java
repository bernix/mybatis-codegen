package cn.bning.codegen.utils;

import java.util.HashMap;

/**
 * @author Bernix Ning
 * @date 2018-12-17
 */
public class HashMapBuilder<K, V> {

    private HashMap<K, V> map;

    private HashMapBuilder(K key, V value) {
        this(16, key, value);
    }

    private HashMapBuilder(int initialCapacity, K key, V value) {
        map = new HashMap<>(initialCapacity);
        map.put(key, value);
    }

    public static <K, V> HashMapBuilder<K, V> newMap(K key, V value) {
        return new HashMapBuilder<>(key, value);
    }

    public static <K, V> HashMapBuilder<K, V> newMap(int initialCapacity, K key, V value) {
        return new HashMapBuilder<>(initialCapacity, key, value);
    }

    public HashMapBuilder<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public HashMap<K, V> build() {
        return map;
    }
}
