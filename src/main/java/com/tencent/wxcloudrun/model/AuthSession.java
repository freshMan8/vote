package com.tencent.wxcloudrun.model;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

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

    Map<String,Object> objectMap = Maps.newHashMap();

    public AuthSession(User user) {
        name = user.getUserName();
        phoneNum = user.getPhoneNum();
    }
}
