package com.tencent.wxcloudrun.service;

import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.dto.EditUserRequest;
import com.tencent.wxcloudrun.dto.UserListRequest;
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

    Integer updateUser(EditUserRequest request);

    PageInfo<User> getUserList(UserListRequest request);

    Integer updateEntity(User user);

    Integer deleteEntity(User user);
}
