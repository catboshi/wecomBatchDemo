package tech.wedev.wecombatch.standard.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wedev.wecombatch.dao.ScheduledLockMapper;
import tech.wedev.wecombatch.entity.po.ScheduledLock;
import tech.wedev.wecombatch.standard.DistributedLockService;

import java.util.Date;

/**
 * 基于数据库实现的分布式锁
 */
@Slf4j
@Service
public class DistributedLockServiceImpl implements DistributedLockService {

    @Autowired
    private ScheduledLockMapper scheduledLockMapper;

    @Override
    public Boolean acquire(String lockName) {
        try {
            ScheduledLock lock = new ScheduledLock();
            lock.setLockName(lockName);
            lock.setModifyTime(new Date());
            lock.setCreateTime(new Date());
            if (scheduledLockMapper.insert(lock) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("获取锁失败：", lockName, e);
            return false;
        }
    }

    @Override
    public Boolean release(String lockName) {
        try {
            int delCount = scheduledLockMapper.deleteLock(lockName);
            int selCount = scheduledLockMapper.count(lockName);

            if (delCount > 0) {
                return true;
            } else if (selCount == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("释放锁失败：", lockName, e);
            return false;
        }
    }

    @Override
    public Boolean delayRelease(String lockName) throws InterruptedException {
        Thread.sleep(10 * 1000);
        return this.release(lockName);
    }

}
