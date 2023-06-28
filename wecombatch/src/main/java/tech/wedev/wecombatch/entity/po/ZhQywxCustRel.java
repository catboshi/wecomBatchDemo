package tech.wedev.wecombatch.entity.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * zh_qywx_cust_rel
 */
@Data
public class ZhQywxCustRel extends BasePO implements Serializable {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 成员微信ID（企业微信）
     */
    private String qywxMgrId;

    /**
     * 成员名称（企业微信）
     */
    private String qywxMgrName;

    /**
     * 客户微信ID（企业微信）
     */
    private String qywxCustId;

    /**
     * 客户名称（企业微信）
     */
    private String qywxCustName;

    /**
     * 客户头像
     */
    private String avatar;

    /**
     * 客户类型
     */
    private String type;

    /**
     * 成员备注
     */
    private String remark;

    /**
     * 客户性别
     */
    private String gender;

    /**
     * 添加好友时间
     */
    private Date addTime;

    /**
     * 客户在微信开放平台的唯一身份标识
     */
    private String unionId;

    /**
     * 客户职位
     */
    private String position;

    /**
     * 客户所在企业主体简称，仅当客户类型是企业微信用户时返回
     */
    private String corpName;

    /**
     * 客户所在企业主体名称，仅当客户类型是企业微信用户时返回
     */
    private String corpFullName;

    /**
     * 客户自定义展示信息，仅当客户类型是企业微信用户时返回
     */
    private String externalProfile;

    /**
     * 客户经理添加客户的方式：0-未知，1-二维码，2-搜索手机号，3-名片分享，4-群聊，5-手机通讯录，6-微信联系人 ，7-来自微信的添加好友申请，8-安装第三方应用时自动添加的客服人员，9-搜索邮箱，201-内部成员共享，202-管理员/负责人分配
     */
    private Integer addWay;

    /**
     * 发起添加的userid，如果成员主动添加，为成员的userid：如果时客户主动添加，则为客户的外部联系人userid；如果时内部成员共享/管理员分配，则为对应的成员/管理员userid
     */
    private String operUserId;

    /**
     * 客户经理给客户的描述
     */
    private String description;

    /**
     * 客户经理给客户备注的公司名
     */
    private String remarkCorpName;

    /**
     * 客户经理给客户备注的手机号
     */
    private String remarkMobiles;

    /**
     * 企业自定义的state参数，用于区分客户具体时通过哪个“联系我”添加，由企业通过创建“联系人”方式指定
     */
    private String state;

    /**
     * 机构号
     */
    private String orgCode;

    private Integer sessionStatus;

    private Integer agreeStatus;

    private Date statusTime;
}
