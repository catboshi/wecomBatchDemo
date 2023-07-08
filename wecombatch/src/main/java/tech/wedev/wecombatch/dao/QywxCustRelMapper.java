package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecombatch.entity.po.QywxCustRel;

import java.util.List;

public interface QywxCustRelMapper {

    List<QywxCustRel> selectByQywxMgrIdAndQywxCustIdAndCorpId(@Param("corpId") String corpId, @Param("qywxMgrId") String qywxMgrId, @Param("qywxCustId") String qywxCustId);

    int updateCustRemark(QywxCustRel qywxCustRel);

    int deleteByQywxMgrIdAndQywxCustId(@Param("qywxMgrId") String qywxMgrId, @Param("qywxCustId") String qywxCustId);

    int deleteBySelective(@Param("qywxCustRelList") List<QywxCustRel> qywxCustRelList);

    int insertBatch(@Param("qywxCustRelList") List<QywxCustRel> qywxCustRelList);

}
