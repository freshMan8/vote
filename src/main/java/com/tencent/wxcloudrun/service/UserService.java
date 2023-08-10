package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.User;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/3 19:18
 */
public interface UserService {

    User getUserByPhoneNum(String phoneNum);

    User createUserByPhone(String phone);
}
