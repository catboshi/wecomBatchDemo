package tech.wedev.wecombatch.standard;

/**
 * 基于数据库实现的分布式锁服务
 */
public interface DistributedLockService {
    Boolean acquire(String lockName);

    Boolean release(String lockName);

    Boolean delayRelease(String lockName) throws InterruptedException;

}
