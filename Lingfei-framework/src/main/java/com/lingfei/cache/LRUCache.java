package com.lingfei.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.lingfei.constants.SystemConstants.LRU_MAX_SIZE;

public class LRUCache extends LinkedHashMap {

    /**
     * 可重入读写锁，保证并发读写安全性
     */
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();
    private static volatile LRUCache lruCache;
    /**
     * 缓存大小限制
     */
    private static final int maxSize = LRU_MAX_SIZE;

//    public LRUCache(int maxSize) {
//        super(maxSize + 1, 1.0f, true);
//        this.maxSize = maxSize;
//    }
    private LRUCache(){
        new LRUCache(maxSize);
    }
    private LRUCache(int maxSize){
        super(maxSize + 1, 1.0f, true);
    }

    public static LRUCache getInstance(){
        if(lruCache==null){
            synchronized (LRUCache.class){
                if(lruCache==null){
                    lruCache = new LRUCache(LRU_MAX_SIZE);
                }
            }
        }
        return lruCache;

    }

    @Override
    public Object get(Object key) {
        readLock.lock();
        try {
            return super.get(key);
        }

        finally {
            readLock.unlock();
        }
    }

    @Override
    public Object put(Object key, Object value) {
        writeLock.lock();
        try {
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    // 只需要覆盖LinkedHashMap的removeEldestEntry方法，便可实现LRU淘汰策略
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > maxSize;
    }
}