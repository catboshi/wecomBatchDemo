package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecombatch.entity.po.QywxAppMsgDetailPO;

import java.util.List;

public interface QywxAppMsgDetailMapper {

    int insert(QywxAppMsgDetailPO qywxAppMsgDetailPO);

    int insertBatch(@Param("qywxAppMsgDetailList") List<QywxAppMsgDetailPO> qywxAppMsgDetailList);

    int updateByArticleSource(QywxAppMsgDetailPO qywxAppMsgDetailPO);

    List<QywxAppMsgDetailPO> selectAllByAppMsgId(@Param("appMsgId") String appMsgId);

    Long selectMinIdByAppMsgId(@Param("appMsgId") String appMsgId);

    List<String> batchSelect1000ByAppMsgId(@Param("appMsgId") String appMsgId, @Param("min") Long min, @Param("max") Long max);

    QywxAppMsgDetailPO selectOneByArticleSource(@Param("articleSource") String articleSource);

    int batchUpdateStatus(@Param("appMsgId") String appMsgId, @Param("newStatus") String newStatus,
                          @Param("oldStatus") String oldStatus,
                          @Param("min") Long min,
                          @Param("max") Long max,
                          @Param("result") String result);

    int batchUpdateStatusByUserId(@Param("appMsgId") String appMsgId, @Param("list") List<String> userIdList,
                                  @Param("newStatus") String newStatus,
                                  @Param("oldStatus") String oldStatus,
                                  @Param("min") Long min,
                                  @Param("max") Long max,
                                  @Param("result") String result);
}
