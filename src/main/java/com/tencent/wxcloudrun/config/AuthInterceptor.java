package com.tencent.wxcloudrun.config;

import com.tencent.wxcloudrun.annotation.AdminCheck;
import com.tencent.wxcloudrun.annotation.OpenApi;
import com.tencent.wxcloudrun.contants.CommonConstant;
import com.tencent.wxcloudrun.contants.ErrorEnum;
import com.tencent.wxcloudrun.exception.VoteExceptionFactory;
import com.tencent.wxcloudrun.model.AuthSession;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserService;
import com.tencent.wxcloudrun.util.TokenUtil;
import com.tencent.wxcloudrun.util.VoteContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法权限注解
            OpenApi openApi = handlerMethod.getMethodAnnotation(OpenApi.class);
            //判断是否是开放接口
            if (openApi != null) {
                return true;
            }
        } else {
            return true;
        }
        String token = request.getHeader(CommonConstant.TOKEN);
        String phoneNum = TokenUtil.INSTANCE.getPhoneNumByAuthToken(token);
        User user = userService.getUserByPhoneNum(phoneNum);
        if (user == null) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0003);
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AdminCheck adminCheck = handlerMethod.getMethodAnnotation(AdminCheck.class);
        if (adminCheck != null && user.getUserType() != -1) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0011);
        }
        VoteContext.setSession(new AuthSession(user));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //清除会话上下文环境
        VoteContext.remove();
    }
}
