package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.OpenApi;
import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.dto.LoginRequest;
import com.tencent.wxcloudrun.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/3 20:08
 */
@RestController
@Slf4j
@RequestMapping(value = "/passport")
public class AuthController {

    @PostMapping(value = "/login")
    @OpenApi
    public ApiResponse loginByCode(@RequestBody LoginRequest request) {
        return ApiResponse.ok(TokenUtil.INSTANCE.getPhoneNumByWX(request.getCode()));
    }
}
