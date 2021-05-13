package top.cookizi.bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@EnableAsync
public class SpringTaskScheduleConfig {

    @Value("${task.executor.core-pool-size}")
    private int corePoolSize;
    @Value("${task.executor.max-pool-size}")
    private int maxPoolSize;
    @Value("${task.executor.thread-name-prefix}")
    private String executorNamePrefix;
    @Value("${task.scheduler.pool-size}")
    private int poolSize;
    @Value("${task.scheduler.thread-name-prefix}")
    private String schedulerNamePrefix;


    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(20000);
        executor.setKeepAliveSeconds(30000);
        executor.setThreadNamePrefix(executorNamePrefix);
        return executor;
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix(schedulerNamePrefix);
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }
}