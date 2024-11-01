package com.tribune.demo.reporting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class TaskConfig {

    /**
     * As requests come in, threads will be created up to 100 and then tasks
     * will be added to the queue until it reaches 100.
     * When the queue is full new threads will be created up to maxPoolSize.
     * Once all the threads are in use and the queue is full tasks will be rejected.
     * As the queue reduces, so does the number of active threads.
     **/

    @Bean("taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);//starting number of threads
        executor.setMaxPoolSize(40);//maximum number of threads//500
        executor.setQueueCapacity(Integer.MAX_VALUE);//number of blocking tasks to be handled by the thread//max
        executor.setThreadNamePrefix("CustomizedThread-");
        executor.initialize();
        return executor;
    }

}
