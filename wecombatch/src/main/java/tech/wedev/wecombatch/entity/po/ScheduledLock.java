package tech.wedev.wecombatch.entity.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ScheduledLock implements Serializable {
    /**
     * 锁名称
     */
    private String lockName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    private static final long serialVersionUID = 1L;

}
