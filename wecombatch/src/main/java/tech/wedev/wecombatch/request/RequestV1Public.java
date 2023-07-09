package tech.wedev.wecombatch.request;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class RequestV1Public implements Serializable {
    @JSONField(name = "methodtype")
    private String methodType;

    @JSONField(name = "url")
    private String url;

    @JSONField(name = "corpid")
    private String corpId;

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
