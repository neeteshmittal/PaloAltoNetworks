package com.neetesh.problem1.services;

public interface ISelfDeleteMap<K,V> {

    void put(K key, V value, long timeoutMs);

    V get(K key);

    void remove(K key);
}
