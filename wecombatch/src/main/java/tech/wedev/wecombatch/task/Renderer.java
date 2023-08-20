package tech.wedev.wecombatch.task;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wedev.wecombatch.dao.CustMgrMapMapper;
import tech.wedev.wecombatch.entity.po.CustMgrMapPO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static tech.wedev.wecombatch.task.LaunderThrowable.launderThrowable;

@Service
public class Renderer {

    @Autowired
    private CustMgrMapMapper custMgrMapMapper;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public void exportDatas() {
        String corpId = "wwbda3d4206748805c";
        int pageSize = 10000;

        int total = custMgrMapMapper.selectCountCustMgrMapList(corpId);
        System.out.println("total: "+total);
        int pageCount = total / pageSize;

        CompletionService<List<List<CustMgrMapPO>>> completionService =
                new ExecutorCompletionService<>(executor);
        var count = 5;
        //index最大不要超过自定义的线程数
        for (int index = 1; index <= count; index++) {
            int ringer = (index - 1) * ((pageCount + 1) / count);
            int num = index * ((pageCount + 1) / count);
            completionService.submit(() -> {
                List<List<CustMgrMapPO>> listNew = new ArrayList<>();
                for (int pageIndex = ringer; pageIndex < num; pageIndex++) {
                    int minId = custMgrMapMapper.selectMinIdByCustMgrMapList(corpId) - 1;
                    long start = System.nanoTime();
                    var list = custMgrMapMapper.selectCustMgrMapListById(corpId, minId, minId + pageSize);
                    long end = System.nanoTime();
                    System.out.println("根据ID 查询耗时：" + String.format("%.2fs", (end - start) * 1e-9));
                    minId = minId + pageSize;
                    list.parallelStream().forEach(a -> a.setInnerMgrName("张三"));
                    //写入表格
                    System.out.println(">>>>>写入表格操作>>>>>");
                    listNew.add(list);
                }
                System.out.println(">>>>>listNew.size()>>>>>" + listNew.size());
                return listNew;
            });
        }

        try {
            var write = 0;
            for (int pageIndex = 0; pageIndex < 2; pageIndex++) {
                Future<List<List<CustMgrMapPO>>> f = completionService.take();
                List<List<CustMgrMapPO>> custMgrMapList = f.get();
                write = custMgrMapList.size() + write;
            }
            System.out.println("一共写入了：" + write);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }
}
