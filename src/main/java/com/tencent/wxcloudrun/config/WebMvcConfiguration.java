package com.tencent.wxcloudrun.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器
 *
 * @author 3832
 * @date 2023/8/3 15:44
 **/
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${app.excludeInterceptPaths:/async/api/**,/api/file/**}")
    private String excludeInterceptPathStr;

    @Value("${app.interceptPaths:/**}")
    private String interceptPathStr;

    @Autowired
    private AuthInterceptor authInterceptor;

    private static final String REGEX_FLAG = ",";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(authInterceptor);
        //忽略异常路由
        interceptor.excludePathPatterns("/error");
        if (StringUtils.isNotBlank(excludeInterceptPathStr)) {
            for (String item : excludeInterceptPathStr.split(REGEX_FLAG, -1)) {
                //不拦截的路径
                interceptor.excludePathPatterns(item.trim());
            }
        }
        if (StringUtils.isNotBlank(interceptPathStr)) {
            for (String item : interceptPathStr.split(REGEX_FLAG, -1)) {
                //拦截的路径
                interceptor.addPathPatterns(item);
            }
        }
    }
}
