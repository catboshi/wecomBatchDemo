package tech.wedev.wecombatch.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.wedev.autm.asyntask.AsynTaskBean;
import tech.wedev.autm.asyntask.AsynTaskErrInfo;
import tech.wedev.autm.asyntask.api.IAsynTask;
import tech.wedev.wecombatch.dao.*;
import tech.wedev.wecombatch.entity.po.GenParamPO;
import tech.wedev.wecombatch.entity.po.QywxAppMsgDetailPO;
import tech.wedev.wecombatch.entity.po.QywxAppMsgPO;
import tech.wedev.wecombatch.entity.po.WecomMarketArticlePO;
import tech.wedev.wecombatch.exception.ExceptionCode;
import tech.wedev.wecombatch.request.RequestV1Private;
import tech.wedev.wecombatch.request.RequestV1Public;
import tech.wedev.wecombatch.third.WecomRequestService;
import tech.wedev.wecombatch.utils.DateUtils;
import tech.wedev.wecombatch.utils.StringUtils;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("PushImgTextMessageTask")
public class PushImgTextMessageTask implements IAsynTask {

    private static final Integer LOOP_LIMIT = 200;
    private static final Integer MAX_SIZE_PER_INSERT = 10000;

    private final String authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid={?}" +
            "&redirect_uri={?}" +
            "&response_type=code" +
            "&scope=snsapi_base" +
            "&state=STATE" +
            "&agentid={?}#wechat_redirect";

    @Value("${wecom.aliyun.picurl.prefix}")
    private String wecomAliyunPicUrlPrefix;

    @Value("${wecom.saveReadLog.url.prefix}")
    private String saveReadLogUrlPrefix;

    private static final String WECOM_API = "WECOM_API";

    private static final String AGENT_APPLICATION = "AGENT_APPLICATION";

    @Autowired
    private QywxAppMsgMapper qywxMsgMapper;

    @Autowired
    private QywxAppMsgDetailMapper qywxMsgDetailMapper;

    @Autowired
    private GenParamMapper genParamMapper;

    @Autowired
    private CustMgrMapMapper custMgrMapMapper;

    @Autowired
    private WecomMarketArticleMapper wecomMarketArticleMapper;

    @Autowired
    private WecomRequestService wecomRequestService;

    @Override
    public AsynTaskErrInfo executeTask(AsynTaskBean task) {
        AsynTaskErrInfo errInfo = new AsynTaskErrInfo();
        errInfo.setSuccFlag(false);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>发送图文消息推送异步任务开始执行>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            errInfo = doBusiness(task);
        } catch (Exception e) {
            log.error("发送图文消息推送异步任务执行时异常，异常信息: " + e.getMessage());
            errInfo.setSuccFlag(false);
            errInfo.setErrMsg("发送图文消息推送异步任务执行时异常，异常信息: " + e.getMessage() + ">>>" + LocalDateTime.now());
            return errInfo;
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>发送图文消息推送异步任务结束>>>>>>>>>>>>>>>>>>>>>>>");
        return errInfo;
    }

    /**
     * 业务逻辑处理
     */
    private AsynTaskErrInfo doBusiness(AsynTaskBean task) {
        AsynTaskErrInfo errInfo = new AsynTaskErrInfo();
        errInfo.setSuccFlag(false);
        String msgId = task.getRefCol1();//推送消息ID
        String corpId = task.getRefCol2();//租户ID
        String articleSource = task.getRefCol3();
        log.info("发送图文消息推送事件###入参: " + JSONObject.toJSONString(msgId + "_" + corpId + "_" + articleSource));
        try {
            //更新消息通知表处理状态：初始状态-->2-已处理
            qywxMsgMapper.updateStatusById(msgId);
            initQywxAppMsgDetail(msgId, corpId, articleSource);
            log.info("###initQywxAppMsgDetail finished, invokePushApplicationMessage underway###");
            //改成单次查询1000条数据
            //List<QywxMsgDetailPO> poList=QywxMsgDetailMapper.selectAllByMsgId(msgId);
            Long minId;
            Long maxId;
            List<String> userIdList;
            //初始化循环次数，上限200
            int loopCount = 0;
            //先查询初始最小值
            minId = Optional.ofNullable(qywxMsgDetailMapper.selectMinIdByAppMsgId(msgId)).orElseThrow(() -> new Exception("无可推送的成员"));
            //设置查询范围最大值，单次1000条数据；如initId=0、maxId=1000，则查询id范围0-999
            maxId = new BigDecimal(minId).add(new BigDecimal(1000)).longValue();
            //查询第一批数据
            userIdList = qywxMsgDetailMapper.batchSelect1000ByAppMsgId(msgId, minId, maxId);
            //同时满足循环次数不超过200且userIdList有数据
            while (CollectionUtils.isNotEmpty(userIdList) && loopCount < LOOP_LIMIT) {
                //更新消息明细表处理状态：0-待处理-->1-处理中
                batchUpdateData(msgId, "1", "0", minId, maxId, null);
                //过滤userId为null、empty、空白值的数据
                userIdList = userIdList.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
                //成员ID列表用'|'分隔
                String toUserList = StringUtils.join(userIdList, "|");
                //调用企微API，支持成员列表最多1000个，企微侧值返回发送部分失败的成员ID，不返回失败具体原因，若全部发送失败，返回81013
                invokePushApplicationMessage(msgId, corpId, toUserList, minId, maxId);
                //查询第二批数据，最大变最小，向上加1000赋值最大
                minId = maxId;
                maxId = new BigDecimal(minId).add(new BigDecimal(1000)).longValue();
                //minId=1000、maxId=2000，查询id范围1000-1999
                userIdList = qywxMsgDetailMapper.batchSelect1000ByAppMsgId(msgId, minId, maxId);
                //循环次数累计
                loopCount++;
            }
            log.info("###invokePushApplicationMessage success, loopCount:" + loopCount);

        } catch (Exception e) {
            log.error("发送图文消息推送异步任务异常: ", e);
            errInfo.setErrMsg(ExceptionCode.ASYNC_ERROR.getMsg());
            return errInfo;
        }

        log.info("发送图文消息推送异步任务执行成功");
        errInfo.setSuccFlag(true);
        return errInfo;
    }

    //加锁保证自增ID顺序
    private synchronized void initQywxAppMsgDetail(String msgId, String corpId, String articleSource) throws Exception {
        log.info("###initQywxAppMsgDetail start###");
        WecomMarketArticlePO wecomMarketArticlePO = wecomMarketArticleMapper.selectOneByArticleSource(articleSource);
        Optional.ofNullable(wecomMarketArticlePO).orElseThrow(() -> new Exception("资讯不存在！"));
        //TODO 部分推送-初始化消息通知明细表时，按可见范围判断，属于则插入
        String articleVisible = wecomMarketArticlePO.getArticleVisible();
        //全量推送，当前企业用户id下且企微客户user_id非空的客户经理
        //并行流处理采用Vector保证线程安全
        SoftReference<Vector<QywxAppMsgDetailPO>> softReference = new SoftReference<>(new Vector<>());
        Vector<QywxAppMsgDetailPO> vector = softReference.get();
        //流是懒加载，不需要考虑内存问题
        custMgrMapMapper.selectByQywxCorpId(corpId)
                .parallelStream()
                .filter(Objects::nonNull)
                .forEach(a -> vector.add(QywxAppMsgDetailPO.builder()
                        .appMsgId(msgId)
                        .qywxCorpId(corpId)
                        .articleSource(articleSource)
                        .userId(a.getQywxMgrId())
                        .status("0")
                        .result(null)
                        .innerMgrId(a.getInnerMgrId())
                        .createId(0L)
                        .modifiedId(0L)
                        .isDeleted(0)
                        .build()));
        //对数据进行分割，MAX_SIZE_PER_INSERT即每次最大插入记录数
        ListUtils.partition(Objects.requireNonNull(vector), MAX_SIZE_PER_INSERT).parallelStream()
                .filter(CollectionUtils::isNotEmpty)
                .forEach(a -> qywxMsgDetailMapper.insertBatch(a));
        log.info("###initQywxAppMsgDetail success, insertNum=" + Objects.requireNonNull(vector).size());
        vector.clear();
    }

    public void invokePushApplicationMessage(String msgId, String corpId, String toUser, Long min, Long max) throws Exception {
        GenParamPO wecomGenParam4AgentId = new GenParamPO();
        wecomGenParam4AgentId.setParamType(WECOM_API);
        wecomGenParam4AgentId.setParamCode(AGENT_APPLICATION);
        wecomGenParam4AgentId.setCorpId(corpId);
        List<GenParamPO> genParamPO4AgentIdList = genParamMapper.selectByParam(wecomGenParam4AgentId);
        log.info("读取配置信息获取企微自建应用AGENT ID" + JSON.toJSONString(genParamPO4AgentIdList));
        String agentId = genParamPO4AgentIdList.get(0).getParamValue();

        log.info("###推送应用所属租户CORP_ID###" + corpId);
        QywxAppMsgPO qywxAppMsgPO = qywxMsgMapper.selectOneById(msgId);
        Map<String, String> param = new HashMap<>();
        param.put("redirect_uri", URLEncoder.encode(saveReadLogUrlPrefix + "?articleSource=" + qywxAppMsgPO.getArticleSource(), "UTF-8"));
        param.put("agentid", agentId);
        param.put("appid", corpId);
        String redirectUrl = replaceParam(authUrl, param);

        WecomMarketArticlePO wecomMarketArticlePO = wecomMarketArticleMapper.selectOneByArticleSource(qywxAppMsgPO.getArticleSource());
        Map<String, Object> requestMap = new HashMap<>();
        RequestV1Public requestV1Public = new RequestV1Public();
        RequestV1Private requestV1Private = new RequestV1Private();
        requestV1Private.setTouser(toUser);
        requestV1Private.setMsgtype("news");
        requestV1Private.setAgentid(Integer.parseInt(StringUtils.isBlank(agentId) ? "0" : agentId));
        RequestV1Private.Articles articleInfo = new RequestV1Private.Articles();
        articleInfo.setTitle(wecomMarketArticlePO.getArticleTitle());
        articleInfo.setDescription(wecomMarketArticlePO.getArticleAbstract() +
                (StringUtils.isNotBlank(wecomMarketArticlePO.getArticleAbstract()) ? "-" : "") +
                DateUtils.formatDateToStr(wecomMarketArticlePO.getReleaseTime(), "yyyy年MM月dd日"));
        articleInfo.setUrl(redirectUrl);
        String picUrl = "";
        if (qywxAppMsgPO.getCoverUrl().indexOf("/") == 0) {
            picUrl = wecomAliyunPicUrlPrefix + qywxAppMsgPO.getCoverUrl();
        } else {
            picUrl = wecomAliyunPicUrlPrefix + "/" + qywxAppMsgPO.getCoverUrl();
        }
        articleInfo.setPicurl(picUrl);
        articleInfo.setAppid("");
        articleInfo.setPagepath("");
        List<RequestV1Private.Articles> articlesList = new ArrayList<>();
        articlesList.add(articleInfo);
        RequestV1Private.News news = new RequestV1Private.News();
        news.setArticles(articlesList);
        requestV1Private.setNews(news);
        requestV1Public.setCorpId(corpId);
        requestMap.put("Public", requestV1Public);
        requestMap.put("Private", requestV1Private);
        log.info("调用企微API发送图文消息报文: " + JSON.toJSONString(new Object[]{requestMap}));

        Map<String, Object> map = wecomRequestService.pushApplicationMessage(requestMap);
        log.info("发送图文应用消息###调用企微API发送图文消息企微侧数据: " + JSONObject.toJSONString(map));
        String errCode = String.valueOf(map.get("errCode"));//rpc调用返回码
        String errMsg = String.valueOf(map.get("errMsg"));//rpc调用返回信息
        if (!"0".equals(errCode)) {
            log.error("发送图文应用消息###调用企微API响应结果：失败！" +
                    "errCode=" + errCode + ", errMsg=" + errMsg + ", msgId=" + msgId + ", toUser=" + toUser);
            batchUpdateDataByUserId(msgId, toUser, "2", "1", min, max, JSONObject.toJSONString(map));
        } else {
            log.info("发送图文应用消息###调用企微API响应结果：成功！" +
                    "errCode=" + errCode + ", errMsg=" + errMsg + ", msgId=" + msgId + ", toUser=" + toUser);
            batchUpdateDataByUserId(msgId, toUser, "2", "1", min, max, JSONObject.toJSONString(map));
        }
    }

    private void batchUpdateData(String msgId, String newStatus, String oldStatus, Long min, Long max, String result) throws Exception {
        try {
            qywxMsgDetailMapper.batchUpdateStatus(msgId, newStatus, oldStatus, min, max, result);
        } catch (Exception e) {
            log.error("批量更新处理状态，从" + oldStatus + "批量更新成-->" + newStatus + "时异常！异常信息：" + e.getMessage());
            throw new Exception("batchUpdateData 批量更新处理状态异常");
        }
    }

    private void batchUpdateDataByUserId(String msgId, String toUserList, String newStatus, String oldStatus, Long min, Long max, String result) throws Exception {
        try {
            List<String> userIdList = StringUtils.split(toUserList, "\\|");
            qywxMsgDetailMapper.batchUpdateStatusByUserId(msgId, userIdList, newStatus, oldStatus, min, max, result);
        } catch (Exception e) {
            log.error("批量更新处理状态，从" + oldStatus + "批量更新成-->" + newStatus + "时异常！异常信息：" + e.getMessage());
            throw new Exception("batchUpdateDataByUserId 批量更新处理状态异常", e);
        }
    }

    private String replaceParam(String url, Map<String, String> param) {
        for (String k : param.keySet()) {
            if (url.contains(k + "={?}")) {
                url = url.replace(k + "={?}", k + "=" + param.get(k));
            }
        }
        return url;
    }
}
