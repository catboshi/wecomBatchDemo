package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecombatch.entity.po.QywxAppMsgPO;

public interface QywxAppMsgMapper {

    int insert(QywxAppMsgPO qywxAppMsgPO);

    int updateByArticleSource(QywxAppMsgPO qywxAppMsgPO);

    QywxAppMsgPO selectOneByArticleSource(@Param("articleSource") String articleSource);

    QywxAppMsgPO selectOneById(@Param("id") String id);

    int updateStatusById(@Param("id") String id);

}
