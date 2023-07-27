package com.neetesh.problem1.services.impl;

import com.neetesh.problem1.services.ITimeoutManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimeoutManager<K> implements ITimeoutManager<K> {
    private final Map<K, Long> timeoutMap;
    private final Lock lock;

    public TimeoutManager(){
        this.timeoutMap = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public void setTimeout(K key, long timeoutMs) {
        lock.lock();
        try {
            timeoutMap.put(key, System.currentTimeMillis() + timeoutMs);
        } finally {
            lock.unlock();
        }
    }

    public boolean isEntryExpired(K key) {
        lock.lock();
        try {
            Long timeout = timeoutMap.get(key);
            return timeout != null && System.currentTimeMillis() > timeout;
        } finally {
            lock.unlock();
        }
    }

    public void removeTimeout(K key) {
        lock.lock();
        try {
            timeoutMap.remove(key);
        } finally {
            lock.unlock();
        }
    }
}
