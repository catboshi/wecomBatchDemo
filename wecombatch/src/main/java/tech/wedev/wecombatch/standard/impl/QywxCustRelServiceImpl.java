package tech.wedev.wecombatch.standard.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.wedev.wecombatch.dao.ZhQywxCustRelMapper;
import tech.wedev.wecombatch.entity.po.ZhQywxCustRel;
import tech.wedev.wecombatch.standard.QywxCustRelService;

import java.util.List;

@Slf4j
@Service
public class QywxCustRelServiceImpl implements QywxCustRelService {

    @Autowired
    private ZhQywxCustRelMapper qywxCustRelMapper;

    @Autowired
    QywxCustRelService custRelService;

    @Override
    @Transactional
    public int replaceIntoQywxCustRelBatch(List<ZhQywxCustRel> qywxCustRelList) {
        int insertNum = 0;
        //通过唯一约束删除已有数据
        qywxCustRelMapper.deleteBySelective(qywxCustRelList);
        //将数据保存到库中
        insertNum = qywxCustRelMapper.insertBatch(qywxCustRelList);
        return insertNum;
    }

    @Override
    public int insertBatch(List<ZhQywxCustRel> qywxCustRelList) {
        int insertNum = 0;
        try {
            insertNum = custRelService.replaceIntoQywxCustRelBatch(qywxCustRelList);
            log.info("QywxCustRelServiceImpl.insertBatch insertNum: ", insertNum);
        } catch (Exception e) {
            log.error("QywxCustRelServiceImpl.insertBatch操作异常: ", e);
        }
        return insertNum;
    }
}
