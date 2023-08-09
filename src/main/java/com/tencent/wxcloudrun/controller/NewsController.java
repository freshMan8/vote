package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.OpenApi;
import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/9 10:40
 */
@RestController
@RequestMapping(value = "/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping(value = "/pageList")
    @OpenApi
    public ApiResponse getPageList() {
        return ApiResponse.ok(newsService.getNewsPage());
    }
}
