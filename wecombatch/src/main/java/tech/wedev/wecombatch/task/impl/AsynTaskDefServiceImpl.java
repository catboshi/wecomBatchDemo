package tech.wedev.wecombatch.task.impl;

import org.springframework.stereotype.Service;
import tech.wedev.autm.asyntask.entity.AsynTaskDef;
import tech.wedev.autm.asyntask.service.IAsynTaskDefService;
import tech.wedev.wecombatch.dao.IAsynTaskDefMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * 异步任务定义服务类
 *
 * 使用异步框架必须实现的接口 IAsynTaskDefService
 */
@Service
public class AsynTaskDefServiceImpl implements IAsynTaskDefService {

    @Resource
    private IAsynTaskDefMapper asynTaskDefMapper;

    @Override
    public List<AsynTaskDef> queryAll() {
        return asynTaskDefMapper.selectAll();
    }

}
