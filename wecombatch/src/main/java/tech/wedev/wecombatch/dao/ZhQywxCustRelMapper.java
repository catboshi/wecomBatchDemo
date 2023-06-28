package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecombatch.entity.po.ZhQywxCustRel;

import java.util.List;

public interface ZhQywxCustRelMapper {

    List<ZhQywxCustRel> selectByQywxMgrIdAndQywxCustIdAndCorpId(@Param("corpId") String corpId, @Param("qywxMgrId") String qywxMgrId, @Param("qywxCustId") String qywxCustId);

    int updateCustRemark(ZhQywxCustRel qywxCustRel);

    int deleteByQywxMgrIdAndQywxCustId(@Param("qywxMgrId") String qywxMgrId, @Param("qywxCustId") String qywxCustId);

    int deleteBySelective(@Param("qywxCustRelList") List<ZhQywxCustRel> qywxCustRelList);

    int insertBatch(@Param("qywxCustRelList") List<ZhQywxCustRel> qywxCustRelList);

}
