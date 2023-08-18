package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.OpenApi;
import com.tencent.wxcloudrun.dto.ActivityDetailRequest;
import com.tencent.wxcloudrun.dto.ActivityRequest;
import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.dto.JoinActivityRequest;
import com.tencent.wxcloudrun.dto.VoteRequest;
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

    @PostMapping(value = "/view")
    @OpenApi
    public ApiResponse getDetailById(@RequestBody ActivityDetailRequest request) {
        return ApiResponse.ok(activityHeaderService.getDetailById(request));
    }

    @PostMapping(value = "/do")
    public ApiResponse vote(@RequestBody VoteRequest request) {
        activityHeaderService.vote(request);
        return ApiResponse.ok();
    }

    @PostMapping(value = "/detail/context")
    @OpenApi
    public ApiResponse getContextById(@RequestBody ActivityDetailRequest request) {
        return ApiResponse.ok(activityHeaderService.getContextList(request));
    }

    @PostMapping(value = "/join")
    public ApiResponse join(@RequestBody JoinActivityRequest request) {
        activityHeaderService.joinActivity(request);
        return ApiResponse.ok();
    }

    @PostMapping(value = "/vote_detail")
    public ApiResponse getActiveDetail(@RequestBody ActivityDetailRequest request) {
        return ApiResponse.ok(activityHeaderService.getActiveDetail(request));
    }

    @PostMapping(value = "/vote_detail_edit")
    public ApiResponse editActiveDetail(@RequestBody JoinActivityRequest request) {
        activityHeaderService.editActivity(request);
        return ApiResponse.ok();
    }
}
