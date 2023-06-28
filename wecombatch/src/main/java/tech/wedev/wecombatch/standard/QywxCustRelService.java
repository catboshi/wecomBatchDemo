package tech.wedev.wecombatch.standard;

import tech.wedev.wecombatch.entity.po.ZhQywxCustRel;

import java.util.List;

public interface QywxCustRelService {

    //批量replace into （qywx_cust_rel)
    int replaceIntoQywxCustRelBatch(List<ZhQywxCustRel> qywxCustRelList);

    int insertBatch(List<ZhQywxCustRel> qywxCustRelList);
}
