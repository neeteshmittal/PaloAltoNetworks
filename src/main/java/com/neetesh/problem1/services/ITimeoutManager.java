package com.neetesh.problem1.services;

public interface ITimeoutManager<K>{
    void setTimeout(K key, long timeoutMs);
    boolean isEntryExpired(K key);
    void removeTimeout(K key);

}
