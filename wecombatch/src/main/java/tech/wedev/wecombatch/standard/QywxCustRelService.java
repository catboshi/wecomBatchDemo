package tech.wedev.wecombatch.standard;

import tech.wedev.wecombatch.entity.po.QywxCustRel;

import java.util.List;

public interface QywxCustRelService {

    //批量replace into （qywx_cust_rel)
    int replaceIntoQywxCustRelBatch(List<QywxCustRel> qywxCustRelList);

    int insertBatch(List<QywxCustRel> qywxCustRelList);
}
