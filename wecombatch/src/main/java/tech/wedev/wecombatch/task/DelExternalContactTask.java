package tech.wedev.wecombatch.task;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.wedev.autm.asyntask.AsynTaskBean;
import tech.wedev.autm.asyntask.AsynTaskErrInfo;
import tech.wedev.autm.asyntask.api.IAsynTask;
import tech.wedev.wecombatch.dao.QywxCustRelMapper;
import tech.wedev.wecombatch.exception.ExceptionCode;

import java.time.LocalDateTime;

@Slf4j
@Component("DelExternalContactTask")
public class DelExternalContactTask implements IAsynTask {

    @Autowired
    private QywxCustRelMapper qywxCustRelMapper;

    @Override
    public AsynTaskErrInfo executeTask(AsynTaskBean task) {
        AsynTaskErrInfo errInfo = new AsynTaskErrInfo();
        errInfo.setSuccFlag(false);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>企微好友删除事件回调异步任务开始执行>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            errInfo = doBusiness(task);
        } catch (Exception e) {
            log.error("企微好友删除事件回调异步任务执行时异常，异常信息: " + e.getMessage());
            errInfo.setSuccFlag(false);
            errInfo.setErrMsg("企微好友删除事件回调异步任务执行时异常，异常信息: " + e.getMessage() + ">>>" + LocalDateTime.now());
            return errInfo;
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>企微好友删除事件回调异步任务结束>>>>>>>>>>>>>>>>>>>>>>>");
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
        try {
            log.info("删除客户事件回调###入参: " + JSONObject.toJSONString(postMap));
            qywxCustRelMapper.deleteByQywxMgrIdAndQywxCustId(qywxMgrId, qywxCustId);
        } catch (Exception e) {
            log.error("企微删除好友事件回调异步任务异常: ", e);
            errInfo.setErrMsg(ExceptionCode.ASYNC_ERROR.getMsg());
            return errInfo;
        }
        log.info("企微好友删除事件回调异步任务执行成功");
        errInfo.setSuccFlag(true);
        return errInfo;
    }
}
