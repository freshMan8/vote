package com.tencent.wxcloudrun.service;

import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.dto.NewsRequest;
import com.tencent.wxcloudrun.model.News;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/8 8:26
 */
public interface NewsService {

    PageInfo<News> getNewsPage(NewsRequest request);
}
