package tech.wedev.wecombatch.task.impl;

import org.springframework.stereotype.Service;
import tech.wedev.autm.asyntask.entity.AsynTaskConf;
import tech.wedev.autm.asyntask.service.IAsynTaskConfService;
import tech.wedev.wecombatch.dao.IAsynTaskConfMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * 异步任务配置服务类
 *
 * 使用异步框架必须实现的接口 IAsynTaskConfService
 */
@Service
public class AsynTaskConfServiceImpl implements IAsynTaskConfService {

    @Resource
    private IAsynTaskConfMapper asynTaskConfMapper;

    @Override
    public List<AsynTaskConf> queryAll() {
        return asynTaskConfMapper.selectAll();
    }

}
