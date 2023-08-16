package com.tencent.wxcloudrun.config;

import com.tencent.wxcloudrun.dao.ActivityDetailMapper;
import com.tencent.wxcloudrun.model.ActivityDetail;
import com.tencent.wxcloudrun.redis.RedissonLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/14 9:08
 */
@Configuration
public class ExecutorDefine {

    @Bean
    public ThreadPoolTaskExecutor refreshExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setKeepAliveSeconds(60);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(10000);
        executor.setThreadNamePrefix("refresh");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
