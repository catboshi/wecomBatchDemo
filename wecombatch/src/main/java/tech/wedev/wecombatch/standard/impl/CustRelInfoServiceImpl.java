package tech.wedev.wecombatch.standard.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tech.wedev.wecombatch.entity.po.ZhQywxCustRel;
import tech.wedev.wecombatch.standard.CustRelInfoService;
import tech.wedev.wecombatch.utils.StringUtils;

import java.util.*;

@Slf4j
@Service
public class CustRelInfoServiceImpl implements CustRelInfoService {
    @Override
    public ZhQywxCustRel getCustRelByCust(Map<String, Object> map, String userId, String corpId) {
        ZhQywxCustRel qywxCustRel = new ZhQywxCustRel();
        JSONObject data = (JSONObject) map.get("data");
        JSONObject custInfo;
        try {
            custInfo = (JSONObject) data.get("external_contact");
            JSONArray followUsers = (JSONArray) data.get("follow_user");
            JSONObject followUser = null;
            if (followUsers != null) {
                for (int i = 0; i < followUsers.size(); i++) {
                    var followUserTemp = followUsers.getJSONObject(i);
                    var followUserId = (String) followUserTemp.get("userid");
                    //找到userid对应的客户经理
                    if (followUserId.equals(userId)) {
                        followUser = followUserTemp;
                        break;
                    }
                }
            }
            /**
             * external_userid(qywx_cust_id) 客户的微信ID
             * name(qywx_cust_name) 客户微信名
             * userId(qywx_mgr_id) 客户经理企微ID
             * follow_user.remark(remark) 该成员对此外部联系人的备注
             * follow_user.description(description)该成员对此外部联系人的描述
             * follow_user.createTime(add_time) 该成员添加此外部联系人的时间
             * follow_user.remark_corp_name(remark_corp_name) 该成员添加此客户备注的企业名称
             * follow_user.remark_mobiles(remark_mobiles) 该成员添加此客户备注的手机号码，第三方不可获取
             * follow_user.aadd_way(add_way) 该成员添加此客户的来源，具体含义详见来源定义
             * follow_user.oper_userid(oper_userid) 发起添加的userid
             * follow_user.state(state) 企业自定义的state参数
             */
            qywxCustRel.setQywxMgrId(userId);
            qywxCustRel.setCorpId(corpId);
            qywxCustRel.setQywxCustId((String) custInfo.get("external_userid"));
            qywxCustRel.setQywxCustName((String) custInfo.get("name"));
            qywxCustRel.setPosition((String) custInfo.get("position"));
            qywxCustRel.setAvatar((String) custInfo.get("avatar"));
            qywxCustRel.setCorpName((String) custInfo.get("corp_name"));
            qywxCustRel.setCorpFullName((String) custInfo.get("corp_full_name"));
            qywxCustRel.setType(String.valueOf(custInfo.get("type")));
            qywxCustRel.setGender(String.valueOf(custInfo.get("gender")));
            qywxCustRel.setUnionId((String) custInfo.get("unionid"));
            if (ObjectUtils.isEmpty(followUser)) {
                //企微侧未返回follow_user
                log.error("CustRelInfoServiceImpl###获取客户关系：该客户与userid为[" + userId + "]的客户经理未添加好友关系");
            }
            if (followUser != null) {
                Integer addWaay = (Integer) followUser.get("add_way");
                String state = (String) followUser.get("state");
                setCustRel(qywxCustRel, followUser, addWaay, state);
            }
            qywxCustRel.setGmtCreate(new Date());
            qywxCustRel.setCreateId(0L);
            qywxCustRel.setGmtModified(new Date());
            qywxCustRel.setModifiedId(0L);
            qywxCustRel.setIsDeleted(0);
        } catch (Exception e) {
            log.error("CustRelInfoService.getCustRelByCust###获取好友关系错误", e);
        }
        return qywxCustRel;
    }

    private void setCustRel(ZhQywxCustRel qywxCustRel, JSONObject followUser, Integer addWay, String state) {
        Integer createTime = (Integer) followUser.get("createtime");
        String remark = (String) followUser.get("remark");
        String description = (String) followUser.get("description");
        String corpName = (String) followUser.get("remark_corp_name");
        JSONArray mobiles = (JSONArray) followUser.get("remark_mobiles");
        String operUserId = (String) followUser.get("oper_userid");
        if (createTime != null) {
            Calendar calendar = Calendar.getInstance();
            long millions = createTime.longValue() * 1000;
            calendar.setTimeInMillis(millions);
            Date addTime = calendar.getTime();
            qywxCustRel.setAddTime(addTime);
        }
        if (addWay != null) {
            qywxCustRel.setAddWay(addWay);
        }
        if (remark != null) {
            qywxCustRel.setRemark(remark);
        }
        if (description != null) {
            qywxCustRel.setDescription(description);
        }
        if (corpName != null) {
            qywxCustRel.setRemarkCorpName(corpName);
        }
        if (mobiles != null) {
            String remarkMobiles = StringUtils.join(mobiles, ",");
            qywxCustRel.setRemarkMobiles(remarkMobiles);
        }
        if (operUserId != null) {
            qywxCustRel.setOperUserId(operUserId);
        }
        if (state != null) {
            qywxCustRel.setState(state);
        }
    }

    @Override
    public List<ZhQywxCustRel> getCustRelByCustList(Map<String, Object> map, String userId, String corpId) {
        List<ZhQywxCustRel> qywxCustRelList = new ArrayList<>();
        JSONObject data = (JSONObject) map.get("data");
        JSONArray externalContactList = (JSONArray) data.get("external_contact_list");
        for (int i = 0; i < externalContactList.size(); i++) {
            try {
                JSONObject custInfos = (JSONObject) externalContactList.get(i);
                JSONObject externalContact = (JSONObject) custInfos.get("external_contact");
                JSONObject followInfo = (JSONObject) custInfos.get("follow_info");
                /**
                 * external_userid(qywx_cust_id) 客户的微信ID
                 * name(qywx_cust_name) 客户微信名
                 * userId(qywx_mgr_id) 客户经理企微ID
                 * follow_user.remark(remark) 该成员对此外部联系人的备注
                 * follow_user.description(description)该成员对此外部联系人的描述
                 * follow_user.createTime(add_time) 该成员添加此外部联系人的时间
                 * follow_user.remark_corp_name(remark_corp_name) 该成员添加此客户备注的企业名称
                 * follow_user.remark_mobiles(remark_mobiles) 该成员添加此客户备注的手机号码，第三方不可获取
                 * follow_user.aadd_way(add_way) 该成员添加此客户的来源，具体含义详见来源定义
                 * follow_user.oper_userid(oper_userid) 发起添加的userid
                 * follow_user.state(state) 企业自定义的state参数
                 */
                ZhQywxCustRel qywxCustRel = new ZhQywxCustRel();
                qywxCustRel.setQywxMgrId(userId);
                qywxCustRel.setQywxCustId((String) externalContact.get("external_userid"));
                qywxCustRel.setQywxCustName((String) externalContact.get("name"));
                qywxCustRel.setPosition((String) externalContact.get("position"));
                qywxCustRel.setAvatar((String) externalContact.get("avatar"));
                qywxCustRel.setCorpName((String) externalContact.get("corp_name"));
                qywxCustRel.setCorpFullName((String) externalContact.get("corp_full_name"));
                qywxCustRel.setType((String) externalContact.get("type"));
                qywxCustRel.setGender((String) externalContact.get("gender"));
                qywxCustRel.setUnionId((String) externalContact.get("unionid"));
                if (ObjectUtils.isEmpty(followInfo)) {
                    //企微侧未返回follow_user
                    log.error("CustRelInfoServiceImpl###获取客户关系：该客户与userid为[" + userId + "]的客户经理未添加好友关系");
                }
                Integer createTime = (Integer) followInfo.get("createtime");
                Integer addWay = (Integer) followInfo.get("add_way");
                String remark = (String) followInfo.get("remark");
                String description = (String) followInfo.get("description");
                String corpName = (String) followInfo.get("remark_corp_name");
                JSONArray mobiles = (JSONArray) followInfo.get("remark_mobiles");
                String state = (String) followInfo.get("state");
                String operUserId = (String) followInfo.get("oper_userid");
                if (createTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    long millions = createTime.longValue() * 1000;
                    calendar.setTimeInMillis(millions);
                    Date addTime = calendar.getTime();
                    qywxCustRel.setAddTime(addTime);
                }
                if (addWay != null) {
                    qywxCustRel.setAddWay(addWay);
                }
                if (remark != null) {
                    qywxCustRel.setRemark(remark);
                }
                if (description != null) {
                    qywxCustRel.setDescription(description);
                }
                if (corpName != null) {
                    qywxCustRel.setRemarkCorpName(corpName);
                }
                if (mobiles != null) {
                    String remarkMobiles = StringUtils.join(mobiles, ",");
                    qywxCustRel.setRemarkMobiles(remarkMobiles);
                }
                if (operUserId != null) {
                    qywxCustRel.setOperUserId(operUserId);
                }
                if (state != null) {
                    qywxCustRel.setState(state);
                }
                qywxCustRel.setGmtCreate(new Date());
                qywxCustRel.setCreateId(0L);
                qywxCustRel.setGmtModified(new Date());
                qywxCustRel.setModifiedId(0L);
                qywxCustRel.setIsDeleted(0);

                qywxCustRelList.add(qywxCustRel);
            } catch (Exception e) {
                log.error("CustRelInfoServiceImpl.getCustRelByCustList###", e);
            }
        }
        return qywxCustRelList;
    }
}
