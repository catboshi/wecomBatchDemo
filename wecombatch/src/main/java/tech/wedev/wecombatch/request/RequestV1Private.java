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

    /**
     * 消息推送-发送应用消息
     */
    @JSONField(name = "touser")
    private String touser;

    @JSONField(name = "toparty")
    private String toparty;

    @JSONField(name = "totag")
    private String totag;

    @JSONField(name = "msgtype")
    private String msgtype;

    @JSONField(name = "agentid")
    private int agentid;

    @JSONField(name = "news")
    private News news;

    @JSONField(name = "enable_id_trans")
    private String enableIdTrans;

    @JSONField(name = "enable_duplicate_check")
    private String enableDuplicateCheck;

    @JSONField(name = "duplicate_check_interval")
    private String duplicateCheckInterval;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public String getEnableIdTrans() {
        return enableIdTrans;
    }

    public void setEnableIdTrans(String enableIdTrans) {
        this.enableIdTrans = enableIdTrans;
    }

    public String getEnableDuplicateCheck() {
        return enableDuplicateCheck;
    }

    public void setEnableDuplicateCheck(String enableDuplicateCheck) {
        this.enableDuplicateCheck = enableDuplicateCheck;
    }

    public String getDuplicateCheckInterval() {
        return duplicateCheckInterval;
    }

    public void setDuplicateCheckInterval(String duplicateCheckInterval) {
        this.duplicateCheckInterval = duplicateCheckInterval;
    }

    public static class News implements Serializable{
        @JSONField(name = "articles")
        private List<Articles> articles;

        public List<Articles> getArticles() {
            return articles;
        }

        public void setArticles(final List<Articles> articles) {
            this.articles = articles;
        }
    }

    public static class Articles implements Serializable{
        /**
         * 标题，不超过128个字节，超过会自动截断
         */
        @JSONField(name = "title")
        private String title;

        /**
         * 描述，不超过512个字节，超过会自动截断
         */
        @JSONField(name = "description")
        private String description;

        /**
         * 点击后跳转的链接
         */
        @JSONField(name = "url")
        private String url;

        /**
         * 图文消息的图片链接，最长2048字节，支持JPG、PNG格式，较好的效果为大图1068*455，小图150*150
         */
        @JSONField(name = "picurl")
        private String picurl;

        /**
         * 小程序appid
         */
        @JSONField(name = "appid")
        private String appid;

        /**
         * 点击消息卡片后的小程序页面
         */
        @JSONField(name = "pagepath")
        private String pagepath;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPagepath() {
            return pagepath;
        }

        public void setPagepath(String pagepath) {
            this.pagepath = pagepath;
        }
    }

}
