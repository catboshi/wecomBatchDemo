package tech.wedev.wecombatch.task.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import tech.wedev.autm.asyntask.api.AsynTaskModule;
import tech.wedev.autm.asyntask.api.AsynTaskMonitor;

import javax.annotation.PreDestroy;

/**
 * 异步组件初始化
 */
@Slf4j
@Component("asynTaskModuleInit")
public class AsynTaskModuleInit implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)
    private AsynTaskMonitor asynTaskMonitor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            log.info("asyn task begin to initialize...");
            //初始化异步框架相关内容
            try {
                AsynTaskModule.getInstance().initialize();
                AsynTaskModule.getInstance().setMonitor(asynTaskMonitor);
            } catch (Throwable e) {
                e.printStackTrace();
                log.error("failed to initialize asyn task. exception: " + e.getMessage());
                return;
            }
            log.info("asyn task initialize successfully");
        }
    }

    @PreDestroy
    public void destroy() {
        //停止异步任务服务
        try {
            AsynTaskModule.getInstance().terminate();
            log.info("异步任务框架关闭成功");
        } catch (Exception e) {
            log.error("异步任务框架关闭时出现异常: ", e);
        }
    }
}
