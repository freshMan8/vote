package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.OpenApi;
import com.tencent.wxcloudrun.dto.ActivityRequest;
import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.dto.GiftRequest;
import com.tencent.wxcloudrun.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/14 15:08
 */
@RestController
@RequestMapping(value = "/gift")
public class GiftController {

    @Autowired
    private GiftService giftService;

    @PostMapping(value = "/list")
    @OpenApi
    public ApiResponse getList(@RequestBody GiftRequest request) {
        return ApiResponse.ok(giftService.pageList(request));
    }
}
