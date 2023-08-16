package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.NewsDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 动态详情
 * @author lf
 * @date 2023-08-10
 */
@Mapper
@Repository
public interface NewsDetailMapper {

    /**
     * 新增
     * @author lf
     * @date 2023/08/10
     **/
    int insert(NewsDetail newsDetail);

    /**
     * 刪除
     * @author lf
     * @date 2023/08/10
     **/
    int delete(Long id);

    /**
     * 更新
     * @author lf
     * @date 2023/08/10
     **/
    int update(NewsDetail newsDetail);

    /**
     * 查询 根据主键 id 查询
     * @author lf
     * @date 2023/08/10
     **/
    NewsDetail load(int id);

    /**
     * 查询 分页查询
     * @author lf
     * @date 2023/08/10
     **/
    List<NewsDetail> pageList(NewsDetail newsDetail);
}
