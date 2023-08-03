package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/login")
    public ApiResponse loginByCode(@RequestParam("code") String code) {
        return ApiResponse.ok(TokenUtil.INSTANCE.getPhoneNumByWX(code));
    }
}
