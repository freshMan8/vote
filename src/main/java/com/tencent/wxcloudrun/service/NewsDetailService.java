package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.NewsDetailRequest;
import com.tencent.wxcloudrun.dto.NewsDetailResponse;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/10 8:29
 */
public interface NewsDetailService {

    NewsDetailResponse getNewsDetail(NewsDetailRequest request);
}
