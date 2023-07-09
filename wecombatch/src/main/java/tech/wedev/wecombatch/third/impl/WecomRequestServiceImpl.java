package tech.wedev.wecombatch.third.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wedev.dubbo.wecom.service.DubboWecomRpcService;
import tech.wedev.wecombatch.request.RequestV1Private;
import tech.wedev.wecombatch.third.WecomRequestService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WecomRequestServiceImpl implements WecomRequestService {

    @Autowired
    private DubboWecomRpcService dubboWecomRpcService;

    @Override
    public Map<String, Object> getExternalUserData(String corpId, String externalUserId) {
        //rpc调用企微API获取客户信息
        Map<String, Object> httpDataMap = new HashMap<>();
        RequestV1Private requestV1Private = new RequestV1Private();
        requestV1Private.setCursor("");
        requestV1Private.setExternalUserId(externalUserId);
        requestV1Private.setCorpId(corpId);
        httpDataMap.put("Private", requestV1Private);
        return dubboWecomRpcService.getApiExternalContactGet(httpDataMap);
    }

    @Override
    public Map<String, Object> pushApplicationMessage(Map<String, Object> requestMap) {
        //rpc调用企微API发送应用消息
        return dubboWecomRpcService.getApiPushApplicationMessage(requestMap);
    }

}
