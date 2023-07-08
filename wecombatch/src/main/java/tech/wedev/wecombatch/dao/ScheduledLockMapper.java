package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecombatch.entity.po.ScheduledLock;

import java.util.List;

public interface ScheduledLockMapper {
    int updateScheduledLock(String oldLockName, String newLockName, String currentDate);

    List<ScheduledLock> queryScheduledLock(ScheduledLock scheduledLock);

    int count(String lockName);

    int insert(ScheduledLock record);

    int insertLock(@Param("lockName") String lockName, @Param("currentDate") String currentDate);

    int deleteLock(@Param("lockName") String lockName);

    int deleteLockByCreateTime(String currentDate);

}
