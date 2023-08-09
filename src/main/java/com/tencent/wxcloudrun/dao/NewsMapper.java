package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.News;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/8 8:27
 */
@Mapper
public interface NewsMapper {

    List<News> getEntity(News news);

    Integer updateEntity(News news);

    Integer deleteEntity(News news);

    Integer insertEntity(News news);
}
