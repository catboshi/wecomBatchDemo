package tech.wedev.wecombatch.dao;

import tech.wedev.wecombatch.entity.po.BasePO;
import tech.wedev.wecombatch.entity.qo.BaseQO;

import java.util.List;

/**
 * @see BasicMapper
 * @param <P>
 * @param <Q>
 */
@Deprecated
public interface BaseMapper<P extends BasePO, Q extends BaseQO> {
    List<P> select(Q q);

    int batchDeleteByPrimaryKey(Q q);

    int updateByPrimaryKey(Q q);

    int save(P p);
}
