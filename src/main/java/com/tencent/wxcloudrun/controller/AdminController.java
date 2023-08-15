package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.AdminCheck;
import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.dto.UserListRequest;
import com.tencent.wxcloudrun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @AdminCheck
    @PostMapping(value = "/user_list")
    public ApiResponse getUserList(UserListRequest request) {
        return ApiResponse.ok(userService.getUserList(request));
    }
}
