package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.OpenApi;
import com.tencent.wxcloudrun.dto.ActivityRequest;
import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.service.ActivityHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/9 15:28
 */
@RestController
@RequestMapping(value = "/vote")
public class ActivityController {

    @Autowired
    private ActivityHeaderService activityHeaderService;

    @PostMapping(value = "/list")
    @OpenApi
    public ApiResponse getList(@RequestBody ActivityRequest request) {
        return ApiResponse.ok(activityHeaderService.getList(request));
    }
}
