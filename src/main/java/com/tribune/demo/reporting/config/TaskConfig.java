package com.tribune.demo.reporting.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class TaskConfig implements AsyncConfigurer {

    /**
     * As requests come in, threads will be created up to 100 and then tasks
     * will be added to the queue until it reaches 100.
     * When the queue is full new threads will be created up to maxPoolSize.
     * Once all the threads are in use and the queue is full tasks will be rejected.
     * As the queue reduces, so does the number of active threads.
     **/
    @Bean(name = "taskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);//starting number of threads
        executor.setMaxPoolSize(40);//maximum number of threads//500
        executor.setQueueCapacity(25);//number of blocking tasks to be handled by the thread//max
        executor.setThreadNamePrefix("CustomizedThread-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}
