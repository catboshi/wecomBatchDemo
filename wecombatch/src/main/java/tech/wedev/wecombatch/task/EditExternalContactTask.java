package tech.wedev.wecombatch.task;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.wedev.autm.asyntask.AsynTaskBean;
import tech.wedev.autm.asyntask.AsynTaskErrInfo;
import tech.wedev.autm.asyntask.api.IAsynTask;
import tech.wedev.wecombatch.dao.QywxCustRelMapper;
import tech.wedev.wecombatch.entity.po.QywxCustRel;
import tech.wedev.wecombatch.exception.ExceptionCode;
import tech.wedev.wecombatch.standard.CustRelInfoService;
import tech.wedev.wecombatch.third.WecomRequestService;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component("EditExternalContactTask")
public class EditExternalContactTask implements IAsynTask {

    @Autowired
    private QywxCustRelMapper qywxCustRelMapper;

    @Autowired
    private CustRelInfoService custRelInfoService;

    @Autowired
    private WecomRequestService wecomRequestService;

    @Override
    public AsynTaskErrInfo executeTask(AsynTaskBean task) {
        AsynTaskErrInfo errInfo = new AsynTaskErrInfo();
        errInfo.setSuccFlag(false);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>企微好友编辑事件回调异步任务开始执行>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            errInfo = doBusiness(task);
        } catch (Exception e) {
            log.error("企微好友编辑事件回调异步任务执行时异常，异常信息: " + e.getMessage());
            errInfo.setSuccFlag(false);
            errInfo.setErrMsg("企微好友编辑事件回调异步任务执行时异常，异常信息: " + e.getMessage() + ">>>" + LocalDateTime.now());
            return errInfo;
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>企微好友编辑事件回调异步任务结束>>>>>>>>>>>>>>>>>>>>>>>");
        return errInfo;
    }

    /**
     * 业务逻辑处理
     */
    private AsynTaskErrInfo doBusiness(AsynTaskBean task) {
        AsynTaskErrInfo errInfo = new AsynTaskErrInfo();
        errInfo.setSuccFlag(false);
        String qywxMgrId = task.getRefCol1();//userid --> 成员ID
        String qywxCustId = task.getRefCol2();//externalUserId --> 客户ID
        String postMap = task.getRefCol3();
        log.info("编辑客户事件回调###入参: " + JSONObject.toJSONString(postMap));
        Map map = JSONObject.parseObject(postMap, HashMap.class);
        try {
            //微信回调信息中的ToUserName为corpId
            String corpId = Optional.ofNullable(map.get("ToUserName")).map(String::valueOf).orElse("");
            getQywxData(qywxMgrId, qywxCustId, corpId);
        } catch (Exception e) {
            log.error("企业微信编辑好友事件回调异步任务异常: ", e);
            errInfo.setErrMsg(ExceptionCode.ASYNC_ERROR.getMsg());
            return errInfo;
        }

        log.info("企微好友编辑事件回调异步任务执行成功");
        errInfo.setSuccFlag(true);
        return errInfo;
    }

    public QywxCustRel getQywxData(String qywxMrgId, String qywxCustId, String corpId) throws Exception {
        List<QywxCustRel> qywxCustRelList = new ArrayList<>();
        Map<String, Object> map = wecomRequestService.getExternalUserData(corpId, qywxCustId);
        log.info("编辑客户事件回调###调用企微API获取客户企微侧数据: " + JSONObject.toJSONString(map));
        String errcode = String.valueOf(map.get("errCode"));
        if (!"0".equals(errcode)) {
            log.error("编辑客户事件回调###调用企微API获取客户企微侧数据失败: " + errcode);
            return null;
        } else {
            log.info("编辑客户事件回调###qywx_cust_rel更新关系信息，getCustRelInfo入参: \"qywxMgrId\": " + qywxMrgId + ", \"qywxCustId\": " + qywxCustId);
            QywxCustRel qywxCustRel = custRelInfoService.getCustRelByCust(map, qywxMrgId, corpId);
            log.info("编辑客户事件回调###qywx_cust_rel更新关系信息: " + JSONObject.toJSONString(qywxCustRel));
            qywxCustRelList.add(qywxCustRel);
            qywxCustRelMapper.selectByQywxMgrIdAndQywxCustIdAndCorpId(corpId, qywxMrgId, qywxCustId)
                    .stream()
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresent(a -> {
                        qywxCustRelMapper.updateCustRemark(qywxCustRel);
                    });
            return qywxCustRel;
        }
    }
}
