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
@TableName("qywx_app_msg")
public class QywxAppMsgPO implements Serializable {

    private static final long serialVersionUID = -6909416296810517055L;

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 租户ID
     */
    private String qywxCorpId;
    /**
     * 资源编号
     */
    private String articleSource;
    /**
     * 资讯标题
     */
    private String title;
    /**
     * 资讯原始链接地址
     */
    private String url;
    /**
     * 封面路径
     */
    private String coverUrl;
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
