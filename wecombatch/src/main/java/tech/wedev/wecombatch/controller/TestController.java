package tech.wedev.wecombatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wedev.dubbo.wecom.service.dubboWecomRpcService;
import tech.wedev.wecombatch.request.RequestV1Private;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private dubboWecomRpcService dubboWecomRpcService;

    @RequestMapping("/externalcontact/get/{external_userid}")
    public Map<String, Object> getExternalContact(@PathVariable String external_userid) {
        HashMap<String, Object> httpDataMap = new HashMap<>();
        RequestV1Private requestV1Private = new RequestV1Private();
        requestV1Private.setCursor("");
        requestV1Private.setExternalUserId(external_userid);
        requestV1Private.setCorpId("wwbda3d4206748805c");
        httpDataMap.put("Private", requestV1Private);
        return dubboWecomRpcService.getApiExternalContactGet(httpDataMap);
    }
}
