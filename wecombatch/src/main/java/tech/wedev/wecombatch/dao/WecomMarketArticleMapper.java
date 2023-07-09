package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecombatch.entity.po.WecomMarketArticlePO;
import tech.wedev.wecombatch.entity.qo.WecomMarketArticleQO;


public interface WecomMarketArticleMapper extends BasicMapper<WecomMarketArticlePO, WecomMarketArticleQO> {
    WecomMarketArticlePO selectOneByArticleSource(@Param("articleSource") String articleSource);
}