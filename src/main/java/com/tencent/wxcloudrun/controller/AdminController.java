package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.AdminCheck;
import com.tencent.wxcloudrun.annotation.OpenApi;
import com.tencent.wxcloudrun.dto.ActivityDetailRequest;
import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.dto.NewsRequest;
import com.tencent.wxcloudrun.dto.NewsStatusRequest;
import com.tencent.wxcloudrun.dto.UpdateActivityDetailRequest;
import com.tencent.wxcloudrun.dto.UpdateNewsRequest;
import com.tencent.wxcloudrun.dto.UserListRequest;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.ActivityHeaderService;
import com.tencent.wxcloudrun.service.NewsService;
import com.tencent.wxcloudrun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/15 11:35
 */
@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private ActivityHeaderService activityHeaderService;

    @AdminCheck
    @PostMapping(value = "/user_list")
    public ApiResponse getUserList(@RequestBody UserListRequest request) {
        return ApiResponse.ok(userService.getUserList(request));
    }

    @AdminCheck
    @PostMapping(value = "/user_update")
    public ApiResponse updateUser(@RequestBody User user) {
        user.setUpdatedBy("admin");
        return ApiResponse.ok(userService.updateEntity(user));
    }

    @AdminCheck
    @PostMapping(value = "/user_del")
    public ApiResponse deleteUser(@RequestBody User user) {
        return ApiResponse.ok(userService.deleteEntity(user));
    }


    @PostMapping(value = "/news_list")
    @AdminCheck
    public ApiResponse getPageList(@RequestBody NewsRequest request) {
        return ApiResponse.ok(newsService.getAllNewsPage(request));
    }

    @PostMapping(value = "/news_status")
    @AdminCheck
    public ApiResponse updateNewsStatus(@RequestBody NewsStatusRequest request) {
        return ApiResponse.ok(newsService.updateStatus(request));
    }

    @PostMapping(value = "/news_sort")
    @AdminCheck
    public ApiResponse updateNewsSort(@RequestBody NewsStatusRequest request) {
        return ApiResponse.ok(newsService.updateStatus(request));
    }

    @PostMapping(value = "/news_edit")
    @AdminCheck
    public ApiResponse updateNewsInfo(@RequestBody UpdateNewsRequest request) {
        return ApiResponse.ok(newsService.updateNewsInfo(request));
    }

    @PostMapping(value = "/vote_list")
    @AdminCheck
    public ApiResponse getActivityList(@RequestBody NewsRequest request) {
        return ApiResponse.ok(activityHeaderService.getAdminList(request));
    }

    @PostMapping(value = "vote_status")
    @AdminCheck
    public ApiResponse updateActivityStatus(@RequestBody NewsStatusRequest request) {
        return ApiResponse.ok(activityHeaderService.updateStatus(request));
    }

    @PostMapping(value = "/vote_detail_stat")
    @AdminCheck
    public ApiResponse getActivityDetail(@RequestBody ActivityDetailRequest request) {
        return ApiResponse.ok(activityHeaderService.setDetailContent(activityHeaderService.getDetailById(request)));
    }

    @PostMapping(value = "/vote_edit")
    @AdminCheck
    public ApiResponse updateActivity(@RequestBody UpdateActivityDetailRequest request) {
        return ApiResponse.ok(activityHeaderService.updateActivityDetail(request));
    }
}
