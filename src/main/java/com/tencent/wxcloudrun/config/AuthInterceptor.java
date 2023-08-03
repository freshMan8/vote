package com.tencent.wxcloudrun.config;

import com.tencent.wxcloudrun.contants.CommonConstant;
import com.tencent.wxcloudrun.redis.RedisService;
import com.tencent.wxcloudrun.redis.RedissonLockService;
import com.tencent.wxcloudrun.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录接口拦截
 *
 * @author 3832
 * @date 2023/8/3 15:36
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(CommonConstant.TOKEN);
        String phoneNum = TokenUtil.INSTANCE.getPhoneNum(token);
        String key = RedissonLockService.getLockKey(CommonConstant.USER_INFO,phoneNum);
        Object obj = redisService.get(key);

        return true;
    }
}
