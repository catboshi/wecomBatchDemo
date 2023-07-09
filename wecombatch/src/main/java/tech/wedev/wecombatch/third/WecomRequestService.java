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

    /**
     * 发送应用消息
     * @param requestMap
     * @return
     * @throws Exception
     */
    Map<String, Object> pushApplicationMessage(Map<String, Object> requestMap) throws Exception;
}
