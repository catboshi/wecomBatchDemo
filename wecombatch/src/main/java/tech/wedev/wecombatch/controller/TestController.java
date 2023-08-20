package tech.wedev.wecombatch.controller;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wedev.dubbo.wecom.service.DubboWecomRpcService;
import tech.wedev.wecombatch.annos.StopWatch;
import tech.wedev.wecombatch.dao.CustMgrMapMapper;
import tech.wedev.wecombatch.request.RequestV1Private;
import tech.wedev.wecombatch.task.Renderer;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private DubboWecomRpcService dubboWecomRpcService;
    @Autowired
    CustMgrMapMapper custMgrMapMapper;
    @Autowired
    Renderer renderer;

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

    /**
     * LIMIT总是设定为pageSize；
     * OFFSET计算公式为pageSize * (pageIndex - 1)
     * 150000 / 10000 = 15 (N)
     * 10000 * (N -1)
     */
    @StopWatch
    @RequestMapping("/a")
    public void export() {
        String corpId = "wwbda3d4206748805c";
        int pageSize = 10000;

        int total = custMgrMapMapper.selectCountCustMgrMapList(corpId);
        System.out.println("total: "+total);
        int pageCount = total / pageSize;
        var write = 0;
        for (int pageIndex = 0; pageIndex < pageCount + 1; pageIndex++) {
            long start = System.nanoTime();
            var list = custMgrMapMapper.selectCustMgrMapList(corpId, pageSize, pageIndex * pageSize);
            long end = System.nanoTime();
            System.out.println("limit 查询耗时：" + String.format("%.2fs", (end - start) * 1e-9));
            write = list.size() + write;
            //赋值操作
            System.out.println("赋值操作：" + pageIndex);
            list.parallelStream().forEach(a -> a.setInnerMgrName("张三"));
            //写入表格
            System.out.println("写入表格操作：" + pageIndex);
        }
        System.out.println("一共写入了：" + write);
    }

    @StopWatch
    @RequestMapping("/b")
    public void export2() {
        String corpId = "wwbda3d4206748805c";
        int pageSize = 1000;

        int total = custMgrMapMapper.selectCountCustMgrMapList(corpId);
        System.out.println("total: "+total);
        int pageCount = total / pageSize;

        long start_ = System.nanoTime();
        var write = 0;
        int minId = custMgrMapMapper.selectMinIdByCustMgrMapList(corpId) - 1;
        for (int pageIndex = 0; pageIndex < pageCount + 1; pageIndex++) {
            long start = System.nanoTime();
            var list = custMgrMapMapper.selectCustMgrMapListById(corpId, minId, minId + pageSize);
            long end = System.nanoTime();
            System.out.println("根据ID 查询耗时：" + String.format("%.2fs", (end - start) * 1e-9));
            minId = minId + pageSize;
            write = list.size() + write;
            //赋值操作
            System.out.println("赋值操作：" + pageIndex);
            list.parallelStream().forEach(a -> a.setInnerMgrName("张三"));
            //写入表格
            System.out.println("写入表格操作：" + pageIndex);
        }
        System.out.println("一共写入了：" + write);
        long end_ = System.nanoTime();
        System.out.println("duration耗时：" + String.format("%.2fs", (end_ - start_) * 1e-9));
    }

    @StopWatch
    @RequestMapping("/c")
    public void export3() {
        renderer.exportDatas();
    }
}
