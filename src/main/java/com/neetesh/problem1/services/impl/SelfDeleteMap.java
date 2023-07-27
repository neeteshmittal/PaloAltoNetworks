package com.neetesh.problem1.services.impl;

import com.neetesh.problem1.services.ISelfDeleteMap;
import com.neetesh.problem1.services.ITimeoutManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SelfDeleteMap<K,V> implements ISelfDeleteMap<K,V> {
    private final Map<K,V>  customMap;

    private final Lock lock;

    private final Condition dataModified;

    private final ITimeoutManager<K> timeoutManager;

    public SelfDeleteMap(ITimeoutManager<K> timeoutManager){
        this.timeoutManager = timeoutManager;
        this.customMap = new HashMap<K,V>();
        this.lock = new ReentrantLock();
        this.dataModified = lock.newCondition();
    }

    @Override
    public void put(K key, V value, long timeoutMs) {
        lock.lock();
        try{
            customMap.put(key, value);
            timeoutManager.setTimeout(key, timeoutMs);
            dataModified.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(K key) {
        lock.lock();
        try {
            while (timeoutManager.isEntryExpired(key)) {
                remove(key); // Automatically remove expired entries upon get.
                return null;
            }
            return customMap.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void remove(K key) {
        lock.lock();
        try {
            customMap.remove(key);
            timeoutManager.removeTimeout(key);
        } finally {
            lock.unlock();
        }
    }
}
