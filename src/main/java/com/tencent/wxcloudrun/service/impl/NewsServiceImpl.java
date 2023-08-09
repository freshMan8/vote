package com.tencent.wxcloudrun.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.dao.NewsMapper;
import com.tencent.wxcloudrun.model.News;
import com.tencent.wxcloudrun.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/8 8:27
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public PageInfo<News> getNewsPage() {
        return PageHelper.startPage(1, 10,"create_time desc")
        .doSelectPageInfo(() -> newsMapper.getEntity(new News()));
    }
}
