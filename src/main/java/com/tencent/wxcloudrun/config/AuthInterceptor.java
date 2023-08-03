package com.tencent.wxcloudrun.config;

import com.tencent.wxcloudrun.contants.CommonConstant;
import com.tencent.wxcloudrun.model.AuthSession;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserService;
import com.tencent.wxcloudrun.util.TokenUtil;
import com.tencent.wxcloudrun.util.VoteContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(CommonConstant.TOKEN);
        String phoneNum = TokenUtil.INSTANCE.getPhoneNum(token);
        User user = userService.getUserByPhoneNum(phoneNum);
        VoteContext.setSession(new AuthSession(user));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清除会话上下文环境
        VoteContext.remove();
    }
}
