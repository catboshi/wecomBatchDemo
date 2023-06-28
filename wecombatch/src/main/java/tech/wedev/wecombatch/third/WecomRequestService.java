package tech.wedev.wecombatch.third;

import java.util.Map;

public interface WecomRequestService {
    /**
     * 获取客户详情公共方法
     * @param corpId
     * @param externalUserId
     * @return
     * @throws Exception
     */
    Map<String, Object> getExternalUserData(String corpId, String externalUserId) throws Exception;
}
