package pjh.dividendmanageproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();

        // 코어 개수
        int n = Runtime.getRuntime().availableProcessors();

        // 생성할 스레드 개수
        threadPool.setPoolSize(n + 1);

        threadPool.initialize();

        taskRegistrar.setTaskScheduler(threadPool);

    }
}
