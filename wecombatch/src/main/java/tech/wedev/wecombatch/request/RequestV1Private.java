package tech.wedev.wecombatch.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RequestV1Private implements Serializable {

    @JSONField(name = "external_userid")
    private String externalUserId;

    @JSONField(name = "corpid")
    private String corpId;

    @JSONField(name = "userid")
    private String userId;

    @JSONField(name = "cursor")
    private String cursor;

    @JSONField(name = "userid_list")
    private List<String> userIdList;

}
