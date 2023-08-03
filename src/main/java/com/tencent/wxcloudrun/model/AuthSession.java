package com.tencent.wxcloudrun.model;

import lombok.Data;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/3 17:33
 */
@Data
public class AuthSession {

    private String name;

    private String phoneNum;

    private String wechatNum;
}
