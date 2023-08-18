package com.tencent.wxcloudrun.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.contants.CommonConstant;
import com.tencent.wxcloudrun.contants.ErrorEnum;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.dto.EditUserRequest;
import com.tencent.wxcloudrun.dto.UpdateTypeRequest;
import com.tencent.wxcloudrun.dto.UserListRequest;
import com.tencent.wxcloudrun.exception.VoteExceptionFactory;
import com.tencent.wxcloudrun.model.AuthSession;
import com.tencent.wxcloudrun.model.News;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.redis.RedisService;
import com.tencent.wxcloudrun.redis.RedissonLockService;
import com.tencent.wxcloudrun.service.UserService;
import com.tencent.wxcloudrun.util.VoteContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public User getUserByPhoneNum(String phoneNum) {
        if (StringUtils.isEmpty(phoneNum)) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0002);
        }
        String key = RedissonLockService.getLockKey(CommonConstant.USER_INFO,phoneNum);
        Object obj = redisService.get(key);
        if (obj != null) {
            return (User) obj;
        }
        User userParam = new User();
        userParam.setPhoneNum(phoneNum);
        List<User> userList = userMapper.getEntity(userParam);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }
        User user = userList.get(0);
        redisService.set(key,user);
        redisService.expire(key,24 * 60 * 60);
        return user;
    }

    @Override
    public User createUserByPhone(String phone) {
        User dbUser = getUserByPhoneNum(phone);
        if (dbUser == null) {
            User user = new User();
            user.setPhoneNum(phone);
            user.setAccount(phone);
            String result = StringUtils.overlay(phone, "****", 3, 7);
            user.setUserName("用户"+result);
            user.setUserType(CommonConstant.UserType.REAL);
            user.setCreateBy("admin");
            user.setUpdatedBy("admin");
            user.setPicUrl("../../../images/tabbar/my_cur.png");
            userMapper.insertEntity(user);
            dbUser = getUserByPhoneNum(phone);
        }
        return dbUser;
    }

    @Override
    public Integer updateUser(EditUserRequest request) {
        AuthSession session = VoteContext.session();
        User user = new User();
        user.setUserName(request.getName());
        user.setPicUrl(request.getPicUrl());
        user.setUpdatedBy(session.getPhoneNum());
        user.setId(session.getId());
        Integer result = userMapper.updateEntity(user);
        String key = RedissonLockService.getLockKey(CommonConstant.USER_INFO,session.getPhoneNum());
        redisService.del(key);
        return result;
    }

    @Override
    public PageInfo<User> getUserList(UserListRequest request) {
        User user = new User();
        user.setUserName(request.getSearch());
        if ("status".equals(request.getSortType())) {
            user.setEnable(1);
        }
        String sortStr = "create_time desc";
        if ("newasc".equals(request.getSortVal())) {
            sortStr = "create_time asc";
        }
        List<Integer> typs = Arrays.asList(1,-1);
        return PageHelper.startPage(1, 10000,sortStr)
                .doSelectPageInfo(() -> userMapper.getEntityType(user,typs));
    }

    @Override
    public Integer updateEntity(User user) {
        User userParam = new User();
        userParam.setId(user.getId());
        List<User> userList = userMapper.getEntity(userParam);
        if (!CollectionUtils.isEmpty(userList)) {
            userMapper.updateEntity(user);
            String key = RedissonLockService.getLockKey(CommonConstant.USER_INFO,userList.get(0).getPhoneNum());
            redisService.del(key);
        }
        return 1;
    }

    @Override
    public Integer deleteEntity(User user) {
        if (user.getId() == null) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0005);
        }
        User userParam = new User();
        userParam.setId(user.getId());
        List<User> userList = userMapper.getEntity(userParam);
        if (!CollectionUtils.isEmpty(userList)) {
            userMapper.deleteEntity(user);
            String key = RedissonLockService.getLockKey(CommonConstant.USER_INFO,userList.get(0).getPhoneNum());
            redisService.del(key);
        }
        return 1;
    }

    @Override
    public Integer updateUserType(UpdateTypeRequest request) {
        User userParam = new User();
        userParam.setId(request.getId());
        userParam.setUserType(request.getType());
        userParam.setUpdatedBy("admin");
        userMapper.updateEntity(userParam);

        User userParam1 = new User();
        userParam1.setId(request.getId());
        List<User> userList = userMapper.getEntity(userParam1);
        String key = RedissonLockService.getLockKey(CommonConstant.USER_INFO,userList.get(0).getPhoneNum());
        redisService.del(key);
        return 1;
    }


}
