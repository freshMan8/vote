package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.contants.CommonConstant;
import com.tencent.wxcloudrun.contants.ErrorEnum;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.exception.VoteExceptionFactory;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.redis.RedisService;
import com.tencent.wxcloudrun.redis.RedissonLockService;
import com.tencent.wxcloudrun.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0003);
        }
        User user = userList.get(0);
        redisService.set(key,user);
        return user;
    }
}
