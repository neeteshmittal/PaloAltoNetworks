package com.neetesh.problem1.services.impl2;

import com.neetesh.problem1.services.ISelfDeleteMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SelfDeleteMap2<K,V> implements ISelfDeleteMap<K,V> {

    private final Map<K, V> map;
    private final Map<K, Long> timeoutMap;
    private final ReentrantReadWriteLock lock;

    private final long CLEANUP_INTERVAL = 1000; // Interval to check and remove expired entries (in milliseconds)
    public SelfDeleteMap2(){
        this.map = new HashMap<>();
        this.timeoutMap = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        // Start the cleanup thread
        Thread cleanupThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(CLEANUP_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cleanup();
            }
        });
        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    @Override
    public void put(K key, V value, long timeoutMs) {
        lock.writeLock().lock();
        try {
            long expirationTime = System.currentTimeMillis() + timeoutMs;
            map.put(key, value);
            timeoutMap.put(key, expirationTime);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V get(K key) {
        lock.readLock().lock();
        try {
            Long timeout = timeoutMap.get(key);
            if (timeout == null || System.currentTimeMillis() > timeout) {
                // Entry has expired, remove it.
                removeEntry(key);
                return null;
            }
            return map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void remove(K key) {
        lock.writeLock().lock();
        try {
            removeEntry(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void removeEntry(K key) {
        map.remove(key);
        timeoutMap.remove(key);
    }


    private boolean isExpired(K key) {
        if(timeoutMap.containsKey(key))
            return timeoutMap.get(key) <= System.currentTimeMillis();
        return true; // Already expired if key is not present in timeoutMap
    }

    //  to cleanup the expired keys
    private void cleanup() {
        lock.writeLock().lock();
        try {
            timeoutMap.entrySet().removeIf(a -> isExpired(a.getKey()));
        } finally {
            lock.writeLock().unlock();
        }
    }

}
