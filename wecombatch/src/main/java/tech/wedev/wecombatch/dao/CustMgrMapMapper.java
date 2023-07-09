package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecombatch.entity.po.CustMgrMapPO;
import tech.wedev.wecombatch.entity.qo.CustMgrMapQO;

import java.util.List;


public interface CustMgrMapMapper extends BasicMapper<CustMgrMapPO, CustMgrMapQO> {
    CustMgrMapPO selectByQywxMgrIdAndQywxCorpId(@Param("qywxMgrId") String qywxMgrId, @Param("qywxCorpId") String qywxCorpId);

    List<CustMgrMapPO> selectByQywxCorpId(String corpId);
}