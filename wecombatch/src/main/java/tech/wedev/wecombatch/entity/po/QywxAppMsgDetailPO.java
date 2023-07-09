package tech.wedev.wecombatch.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.wedev.wecombatch.annos.TableName;

import java.io.Serializable;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("qywx_app_msg_detail")
public class QywxAppMsgDetailPO implements Serializable {

    private static final long serialVersionUID = -6909416297590517172L;

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 消息编号
     */
    private String appMsgId;
    /**
     * 租户ID
     */
    private String qywxCorpId;
    /**
     * 资源编号
     */
    private String articleSource;
    /**
     * 企微成员ID
     */
    private String userId;
    /**
     * 统一认证号
     */
    private String innerMgrId;
    /**
     * 处理结果
     */
    private String result;
    /**
     * 处理状态；0-待处理 1-处理中 2-已处理
     */
    private String status;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 修改时间
     */
    private Date gmtModified;
    /**
     * 创建人ID
     */
    private Long createId;
    /**
     * 修改人ID
     */
    private Long modifiedId;
    /**
     * 是否删除，0.存在，1.删除
     */
    private Integer isDeleted;

}
