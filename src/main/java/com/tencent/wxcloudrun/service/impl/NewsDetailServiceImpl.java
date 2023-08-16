package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.contants.ErrorEnum;
import com.tencent.wxcloudrun.dao.NewsDetailMapper;
import com.tencent.wxcloudrun.dao.NewsMapper;
import com.tencent.wxcloudrun.dto.NewsDetailRequest;
import com.tencent.wxcloudrun.dto.NewsDetailResponse;
import com.tencent.wxcloudrun.exception.VoteExceptionFactory;
import com.tencent.wxcloudrun.model.News;
import com.tencent.wxcloudrun.model.NewsDetail;
import com.tencent.wxcloudrun.service.NewsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/10 8:36
 */
@Service
public class NewsDetailServiceImpl implements NewsDetailService {

    @Autowired
    private NewsDetailMapper newsDetailMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public NewsDetailResponse getNewsDetail(NewsDetailRequest request) {
        if (request == null || request.getId() == null) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0005);
        }
        News news = newsMapper.getEntityById(request.getId());
        if (news == null) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0004);
        }
        NewsDetail newsDetail = new NewsDetail();
        newsDetail.setNewsId(request.getId());
        List<NewsDetail> newsDetails = newsDetailMapper.pageList(newsDetail);
        NewsDetailResponse response = new NewsDetailResponse();
        response.setNewsDate(news.getCreateTime());
        response.setTitle(news.getTitle());
        response.setNewsCate("动态");
        response.setContent(newsDetails);
        response.setSort(news.getSort());
        response.setId(news.getId());
        response.setPicUrl(news.getPicUrl());
        response.setContext(news.getContext());

        return response;
    }
}
