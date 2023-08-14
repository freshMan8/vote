package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Gift;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 礼物详情
 * @author lf
 * @date 2023-08-14
 */
@Mapper
@Repository
public interface GiftMapper {

    /**
     * 新增
     * @author lf
     * @date 2023/08/14
     **/
    int insert(Gift gift);

    /**
     * 刪除
     * @author lf
     * @date 2023/08/14
     **/
    int delete(int id);

    /**
     * 更新
     * @author lf
     * @date 2023/08/14
     **/
    int update(Gift gift);

    /**
     * 查询 根据主键 id 查询
     * @author lf
     * @date 2023/08/14
     **/
    Gift load(int id);

    /**
     * 查询 分页查询
     * @author lf
     * @date 2023/08/14
     **/
    List<Gift> pageList(Gift gift);

}
