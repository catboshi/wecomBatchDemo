package tech.wedev.wecombatch.standard;

import tech.wedev.wecombatch.entity.po.QywxCustRel;

import java.util.List;
import java.util.Map;

public interface CustRelInfoService {
    QywxCustRel getCustRelByCust(Map<String, Object> map, String userId, String corpId);

    List<QywxCustRel> getCustRelByCustList(Map<String, Object> map, String userId, String corpId);
}
