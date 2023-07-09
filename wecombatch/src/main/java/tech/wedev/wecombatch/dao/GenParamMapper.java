package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecombatch.entity.po.GenParamPO;
import tech.wedev.wecombatch.entity.qo.GenParamQO;
import tech.wedev.wecombatch.enums.GenParamEnum;

import java.util.List;

public interface GenParamMapper extends BaseMapper<GenParamPO, GenParamQO> {
    int updateWecomGenParam(GenParamQO genParamQO);

    String selectValueByUK(@Param("genParamEnum") GenParamEnum genParamEnum, @Param("corpId") String corpId);

    List<GenParamPO> selectByParam(@Param("wecomGenParam") GenParamPO wecomGenParam);

    List<GenParamQO> selectByParams(@Param("wecomGenParam") GenParamQO wecomGenParam);

    List<GenParamPO> selectByParamType(@Param("wecomGenParam") GenParamQO wecomGenParam);

}
