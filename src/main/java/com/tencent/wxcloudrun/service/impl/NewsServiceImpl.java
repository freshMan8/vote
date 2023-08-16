package com.tencent.wxcloudrun.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.dao.NewsDetailMapper;
import com.tencent.wxcloudrun.dao.NewsMapper;
import com.tencent.wxcloudrun.dto.NewsRequest;
import com.tencent.wxcloudrun.dto.NewsStatusRequest;
import com.tencent.wxcloudrun.dto.UpdateNewsRequest;
import com.tencent.wxcloudrun.model.News;
import com.tencent.wxcloudrun.model.NewsDetail;
import com.tencent.wxcloudrun.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private NewsDetailMapper detailMapper;

    @Override
    public PageInfo<News> getNewsPage(NewsRequest request) {
        News news = new News();
        news.setTitle(request.getSearch());
        news.setEnable(1);
        return PageHelper.startPage(1, 1000,"sort asc")
                .doSelectPageInfo(() -> newsMapper.getEntity(news));
    }

    @Override
    public PageInfo<News> getAllNewsPage(NewsRequest request) {
        News news = new News();
        news.setTitle(request.getSearch());
        if ("status".equals(request.getSortType()) && "1".equals(request.getSortVal())) {
            news.setEnable(1);
        } else if ("status".equals(request.getSortType()) && "0".equals(request.getSortVal())) {
            news.setEnable(0);
        }
        return PageHelper.startPage(1, 1000,"create_time desc")
                .doSelectPageInfo(() -> newsMapper.getEntity(news));
    }

    @Override
    public Integer updateStatus(NewsStatusRequest request) {
        News news = new News();
        news.setId(request.getId());
        news.setSort(request.getSort());
        news.setEnable(request.getStatus());
        news.setUpdatedBy("admin");
        return newsMapper.updateEntity(news);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateNewsInfo(UpdateNewsRequest request) {
        News news = new News();
        news.setTitle(request.getTitle());
        news.setContext(request.getContext());
        news.setPicUrl(request.getPicUrl());
        news.setSort(request.getSort());
        news.setUpdatedBy("admin");
        news.setType("news");
        if (request.getId() == null) {
            news.setEnable(1);
            news.setShortContext(request.getTitle());
            news.setCreateBy("admin");
            newsMapper.insertEntity(news);
        } else {
            news.setId(request.getId());
            newsMapper.updateEntity(news);
        }

        detailMapper.delete(news.getId());
        for (int i = 0; i < request.getContents().size(); i++) {
            NewsDetail newsDetail = new NewsDetail();
            newsDetail.setNewsId(news.getId());
            newsDetail.setContext(request.getContents().get(i).getContext());
            newsDetail.setType(request.getContents().get(i).getType());
            newsDetail.setOrders(i);
            newsDetail.setCreateBy("admin");
            newsDetail.setUpdatedBy("admin");
            detailMapper.insert(newsDetail);
        }
        return 1;
    }
}
