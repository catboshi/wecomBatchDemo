package tech.wedev.wecombatch.standard;

import tech.wedev.wecombatch.entity.po.ZhQywxCustRel;

import java.util.List;
import java.util.Map;

public interface CustRelInfoService {
    ZhQywxCustRel getCustRelByCust(Map<String, Object> map, String userId, String corpId);

    List<ZhQywxCustRel> getCustRelByCustList(Map<String, Object> map, String userId, String corpId);
}
