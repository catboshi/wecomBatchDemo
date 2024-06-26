package tech.wedev.wecombatch.task.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import tech.wedev.autm.asyntask.AsynTaskBean;
import tech.wedev.autm.asyntask.AsynTaskEnum;
import tech.wedev.autm.asyntask.entity.AsynTaskDtl;
import tech.wedev.autm.asyntask.service.IAsynTaskDtlService;
import tech.wedev.wecombatch.dao.IAsynTaskDtlHisMapper;
import tech.wedev.wecombatch.dao.IAsynTaskDtlMapper;
import tech.wedev.wecombatch.utils.ValConvertUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 异步任务明细记录服务类
 *
 * 使用异步框架必须实现的接口 IAsynTaskDtlService
 */
@Service
public class AsynTaskDtlServiceImpl implements IAsynTaskDtlService {

    @Resource
    private IAsynTaskDtlMapper asynTaskDtlMapper;

    @Resource
    private IAsynTaskDtlHisMapper asynTaskDtlHisMapper;

    static AsynTaskBean convertDOtoEntity(AsynTaskDtl asynTaskDtlDO) {
        AsynTaskBean result = new AsynTaskBean();
        result.setTaskId(asynTaskDtlDO.getTaskId());
        result.setKeywords(asynTaskDtlDO.getKeywords());
        result.setTaskType(asynTaskDtlDO.getTaskType());
        result.setSubTaskType(asynTaskDtlDO.getSubTaskType());
        result.setState(asynTaskDtlDO.getState());
        result.setPriority(AsynTaskEnum.TaskPriorityType.getEnum(ValConvertUtils.intToDecimal(asynTaskDtlDO.getPriority())));
        result.setApplyTime(asynTaskDtlDO.getApplyTime());
        result.setInQueueTime(asynTaskDtlDO.getInQueueTime());
        result.setIp(asynTaskDtlDO.getIp());
        result.setThreadId(asynTaskDtlDO.getThreadId());
        result.setStartWorkTime(asynTaskDtlDO.getStartWorkTime());
        result.setEndWorkTime(asynTaskDtlDO.getEndWorkTime());
        result.setErrCode(asynTaskDtlDO.getErrCode());
        result.setErrMsg(asynTaskDtlDO.getErrMsg());
        result.setDealNum(ValConvertUtils.intToDecimal(asynTaskDtlDO.getDealNum()));
        result.setTimeoutLimit(ValConvertUtils.intToDecimal(asynTaskDtlDO.getTimeoutLimit()));
        result.setPlanTime(asynTaskDtlDO.getPlanTime());
        result.setRefCol1(asynTaskDtlDO.getRefCol1());
        result.setRefCol2(asynTaskDtlDO.getRefCol2());
        result.setRefCol3(asynTaskDtlDO.getRefCol3());
        result.setInfo1(asynTaskDtlDO.getInfo1());
        result.setInfo2(asynTaskDtlDO.getInfo2());
        result.setInfo3(asynTaskDtlDO.getInfo3());
        return result;
    }

    @Override
    public int insertAsynTask(AsynTaskBean taskBean) {
        AsynTaskDtl asynTaskDtlDO = convertEntitytoDO(taskBean);
        if (asynTaskDtlMapper.updateAsynTask(asynTaskDtlDO) == 1) {
            return 1;
        }
        return asynTaskDtlMapper.insert(asynTaskDtlDO);
    }

    static AsynTaskDtl convertEntitytoDO(AsynTaskBean taskBean) {
        AsynTaskDtl asynTaskDtlDO = new AsynTaskDtl();
        asynTaskDtlDO.setTaskId(taskBean.getTaskId());
        asynTaskDtlDO.setKeywords(taskBean.getKeywords());
        asynTaskDtlDO.setTaskType(taskBean.getTaskType());
        asynTaskDtlDO.setSubTaskType(taskBean.getSubTaskType());
        asynTaskDtlDO.setState(taskBean.getState());
        asynTaskDtlDO.setPriority(ValConvertUtils.decimalToInt(taskBean.getPriority().getValue()));
        asynTaskDtlDO.setApplyTime(taskBean.getApplyTime());
        asynTaskDtlDO.setInQueueTime(taskBean.getInQueueTime());
        asynTaskDtlDO.setIp(taskBean.getIp());
        asynTaskDtlDO.setThreadId(taskBean.getThreadId());
        asynTaskDtlDO.setStartWorkTime(taskBean.getStartWorkTime());
        asynTaskDtlDO.setEndWorkTime(taskBean.getEndWorkTime());
        asynTaskDtlDO.setErrCode(taskBean.getErrCode());
        asynTaskDtlDO.setErrMsg(taskBean.getErrMsg());
        asynTaskDtlDO.setDealNum(ValConvertUtils.decimalToInt(taskBean.getDealNum()));
        if (taskBean.getTimeoutLimit() != null) {
            asynTaskDtlDO.setTimeoutLimit(ValConvertUtils.decimalToInt(taskBean.getTimeoutLimit()));
        }
        asynTaskDtlDO.setPlanTime(taskBean.getPlanTime());
        asynTaskDtlDO.setRefCol1(taskBean.getRefCol1());
        asynTaskDtlDO.setRefCol2(taskBean.getRefCol2());
        asynTaskDtlDO.setRefCol3(taskBean.getRefCol3());
        asynTaskDtlDO.setInfo1(taskBean.getInfo1());
        asynTaskDtlDO.setInfo2(taskBean.getInfo2());
        asynTaskDtlDO.setInfo3(taskBean.getInfo3());
        return asynTaskDtlDO;
    }

    @Override
    public int updateStateWhenRobTask(String ip, String taskType, int fetchTaskCount) {
        return asynTaskDtlMapper.updateIpByIndex(ip, taskType, fetchTaskCount);
    }

    @Override
    public List<AsynTaskBean> queryRobList(String ip, String taskType) {
        return findAsynTaskBeans(ip, taskType, AsynTaskEnum.TaskStateType.PEDDING);
    }

    /**
     * 把任务的状态置为进入队列和修改时间
     *
     * @param ip
     * @param taskType
     * @return
     */
    @Override
    public int updateRobListAfterPutInQueue(String ip, String taskType) {
        //旧状态为 PEDDING("0", "待处理")
        //即将更新的新状态为 IN_QUEUE("1", "进入队列")
        return asynTaskDtlMapper.
                updateInQueueStateByIndex(AsynTaskEnum.TaskStateType.PEDDING.getCode(),
                        AsynTaskEnum.TaskStateType.IN_QUEUE.getCode(),
                        ip, taskType);
    }

    @Override
    public int updateStateToStart(AsynTaskBean taskBean) {
        /*
         * 先把当前任务状态改为开始处理START_WORK("2", "开始工作")
         * THREAD_ID   工作线程号   VARCHAR2   框架回填字段
         * START_WORK_TIME   开始工作时间   TIMESTAMP   框架回填字段
         */
        return asynTaskDtlMapper.
                updateStartWorkStateByIndex(AsynTaskEnum.TaskStateType.IN_QUEUE.getCode(),
                        AsynTaskEnum.TaskStateType.START_WORK.getCode(),
                        taskBean.getThreadId(),
                        taskBean.getIp(),
                        taskBean.getTaskType());
    }

    @Override
    public int updateStateToSuccess(AsynTaskBean taskBean) {
        /*
         * 结束时，把任务状态改为处理成功SUCC("3", "任务成功")
         * END_WORK_TIME   结束工作时间   TIMESTAMP   框架回填字段
         */
        return asynTaskDtlMapper.
                updateSuccessOrFailStateByIndex(AsynTaskEnum.TaskStateType.START_WORK.getCode(),
                        AsynTaskEnum.TaskStateType.SUCC.getCode(),
                        taskBean);
    }

    @Override
    public int updateStateToFail(AsynTaskBean taskBean) {
        /*
         * 结束时，把任务状态改为处理成功FAIL("4", "任务失败")
         * END_WORK_TIME   结束工作时间   TIMESTAMP   框架回填字段
         */
        return asynTaskDtlMapper.
                updateSuccessOrFailStateByIndex(AsynTaskEnum.TaskStateType.START_WORK.getCode(),
                        AsynTaskEnum.TaskStateType.FAIL.getCode(),
                        taskBean);
    }

    @Override
    public int updateStateToCancel(AsynTaskBean taskBean) {
        AsynTaskDtl asynTaskDtlDOCondition = new AsynTaskDtl();
        asynTaskDtlDOCondition.setTaskId(taskBean.getTaskId());
        asynTaskDtlDOCondition.setErrCode(taskBean.getErrCode());
        asynTaskDtlDOCondition.setErrMsg(taskBean.getErrMsg());
        asynTaskDtlDOCondition.setState(AsynTaskEnum.TaskStateType.REVOKE.getCode());
        return asynTaskDtlMapper.updateRevokeStateByPrimary(asynTaskDtlDOCondition);
    }

    @Override
    public int updateStateToPending(AsynTaskBean taskBean) {
        //把任务状态修改为待处理 PEDDING("0", "待处理")
        AsynTaskDtl asynTaskDtlDOCondition = new AsynTaskDtl();
        asynTaskDtlDOCondition.setTaskId(taskBean.getTaskId());
        asynTaskDtlDOCondition.setState(AsynTaskEnum.TaskStateType.PEDDING.getCode());
        return asynTaskDtlMapper.updateByPrimary(asynTaskDtlDOCondition);
    }

    @Override
    public int updateMyIpTaskStateToPending(AsynTaskBean taskBean) {
        //对应本机IP处理的任务都重置 PEDDING
        return asynTaskDtlMapper.
                updateStateByIpAndTaskType(taskBean.getIp(),
                        taskBean.getTaskType(),
                        AsynTaskEnum.TaskStateType.PEDDING.getCode());
    }

    @Override
    public int updateStateToKill(AsynTaskBean taskBean) {
        /** 因异步任务超时所以被Kill
         * TIME_OUT_KILL("5", "超时被杀")
         * taskBean.setErrCode("C10100002");
         * taskBean.setErrMsg("异常任务超时");
         */
        AsynTaskDtl asynTaskDtlDOCondition = new AsynTaskDtl();
        asynTaskDtlDOCondition.setTaskId(taskBean.getTaskId());
        asynTaskDtlDOCondition.setErrCode(taskBean.getErrCode());
        asynTaskDtlDOCondition.setErrMsg(taskBean.getErrMsg());
        asynTaskDtlDOCondition.setState(AsynTaskEnum.TaskStateType.TIME_OUT_KILL.getCode());
        return asynTaskDtlMapper.updateByPrimary(asynTaskDtlDOCondition);
    }

    @Override
    public int updateStateAndIpWhenDoubleTimeout(String ip, String taskType) {
        return 0;
    }

    @Override
    public List<AsynTaskBean> queryMyIpTimeoutTaskList(String ip, String taskType) {
        //再查找超时时间属于本机IP的任务进行查杀及重做 TIME_OUT_LOCK("6", "超时被抢")
        return findAsynTaskBeans(ip, taskType, AsynTaskEnum.TaskStateType.TIME_OUT_LOCK);
    }

    private List<AsynTaskBean> findAsynTaskBeans(String ip, String taskType, AsynTaskEnum.TaskStateType taskStateType) {
        AsynTaskDtl asynTaskDtlDOCondition = new AsynTaskDtl();
        asynTaskDtlDOCondition.setTaskType(taskType);
        asynTaskDtlDOCondition.setState(taskStateType.getCode());
        asynTaskDtlDOCondition.setIp(ip);
        List<AsynTaskDtl> asynTaskDtls = asynTaskDtlMapper.selectAll(asynTaskDtlDOCondition);

        List<AsynTaskBean> result = new ArrayList<>(asynTaskDtls.size());

        asynTaskDtls.forEach(asynTaskDtl -> {
            result.add(convertDOtoEntity(asynTaskDtl));
        });
        return result;
    }

    @Override
    public int insertSuccessTaskToHis(String taskId) {
        return asynTaskDtlHisMapper.insert(asynTaskDtlMapper.selectByPrimary(taskId));
    }

    @Override
    public int deleteSuccessTask(String taskId) {
        return asynTaskDtlMapper.deleteByPrimary(taskId);
    }

    @Override
    public int insertTaskToHis(String taskId) {
        return asynTaskDtlHisMapper.insert(asynTaskDtlMapper.selectByPrimary(taskId));
    }

    @Override
    public int deleteTask(String taskId) {
        return asynTaskDtlMapper.deleteByPrimary(taskId);
    }

    @Override
    public void batchInsertAsynTask(List<AsynTaskBean> asynTaskBeanList) {
        if (CollectionUtils.isNotEmpty(asynTaskBeanList)) {
            List<AsynTaskDtl> asynTaskDtlList = new ArrayList<>(asynTaskBeanList.size());
            asynTaskBeanList.forEach(asynTaskBean -> {
                asynTaskDtlList.add(convertEntitytoDO(asynTaskBean));
            });
            asynTaskDtlMapper.batchInsert(asynTaskDtlList);
        }
    }
}
