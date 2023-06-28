package tech.wedev.wecombatch.task.factory;

import org.springframework.stereotype.Service;
import tech.wedev.autm.asyntask.api.AbstractAsynTaskServiceFactory;
import tech.wedev.autm.asyntask.api.IAsynTask;
import tech.wedev.autm.asyntask.service.IAsynTaskConfService;
import tech.wedev.autm.asyntask.service.IAsynTaskDefService;
import tech.wedev.autm.asyntask.service.IAsynTaskDtlService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认工厂类
 * <p>
 * 使用异步框架必须要继承的抽象工厂 AbstractAsynTaskServiceFactory
 */
@Service
public class DefaultAsynTaskServiceFactory extends AbstractAsynTaskServiceFactory {

    @Resource
    private IAsynTaskDtlService asynTaskDtlService;

    @Resource
    private IAsynTaskDefService asynTaskDefService;

    @Resource
    private IAsynTaskConfService asynTaskConfService;

    @Resource
    private List<IAsynTask> asynTaskList;//异步任务做的具体的业务相关的实现类

    /*
    业务实现类的具体名称和对应的实现类的对象
    目的是异步框架根据serviceName
    (serviceName取值根据在数据库中SYS_ASYN_TASK_DEF或SYS_ASYN_TASK_CONF
    表中配置的任务实现类字段（TASK_IMPL）为准，并且得对应代码中要有真正已serviceName取值的java类）
    快速定位到对应的具体业务实现类。
     */
    private final Map<String, IAsynTask> asynTaskMap = new HashMap<String, IAsynTask>();

    public List<IAsynTask> getAsynTaskList() {
        return asynTaskList;
    }

    public void setAsynTaskList(List<IAsynTask> asynTaskList) {
        this.asynTaskList = asynTaskList;
    }

    public void setAsynTaskDtlService(IAsynTaskDtlService asynTaskDtlService) {
        this.asynTaskDtlService = asynTaskDtlService;
    }

    public void setAsynTaskDefService(IAsynTaskDefService asynTaskDefService) {
        this.asynTaskDefService = asynTaskDefService;
    }

    public void setAsynTaskConfService(IAsynTaskConfService asynTaskConfService) {
        this.asynTaskConfService = asynTaskConfService;
    }

    @PostConstruct
    public void preWorkAfterInitial() {
        //初始化完成之后，把实现了抽象工厂的具体实现类，传入父类
        //因为这是异步框架能从工厂获得这些自定义服务的方式
        setFactory(this);
        //处理对象的serviceName到asynTaskMap的映射
        handleServiceNameToIAsynTaskMap();
    }

    private void handleServiceNameToIAsynTaskMap() {
        if (asynTaskList != null) {
            asynTaskList.forEach((asynTask) -> {
                String className = asynTask.getClass().getSimpleName();
                //把第一个类名的字母变为小写
                String firstLetterOfClassName = className.substring(0, 1).toLowerCase();
                className = firstLetterOfClassName + className.substring(1, className.length());
                asynTaskMap.put(className, asynTask);
            });
        } else {
            throw new IllegalArgumentException("asynTaskList is null");
        }
    }

    @Override
    public IAsynTaskDtlService getAsynTaskDtlService() {
        return asynTaskDtlService;
    }

    @Override
    public IAsynTaskConfService getAsynTaskConfService() {
        return asynTaskConfService;
    }

    @Override
    public IAsynTaskDefService getAsynTaskDefService() {
        return asynTaskDefService;
    }

    @Override
    public IAsynTask getIAsynTask(String serviceName) throws Exception {
        return asynTaskMap.get(serviceName);
    }
}
