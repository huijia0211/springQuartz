package com.learn.quartz.config;

import com.learn.quartz.constant.ScheduledTaskEnum;
import com.learn.quartz.job.WeatherDataSyncJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Map;
import java.util.Properties;

@Configuration
public class ScheduledTaskConfig {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskConfig.class);

    /**
     * 初始化定时任务Map
     * key :任务key
     * value : 执行接口实现
     */
    @Bean(name = "scheduledTaskJobMap")
    public Map<String, Job> scheduledTaskJobMap() {
        return ScheduledTaskEnum.initScheduledTask();
    }

    public JobDetail weatherDataSyncJobDetail() {
        return JobBuilder.newJob(WeatherDataSyncJob.class).withIdentity("weatherDataSyncJob")
                .storeDurably().build();
    }

    public Trigger weatherDataSyncTrigger() {

        SimpleScheduleBuilder schedBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(2).repeatForever();

        return TriggerBuilder.newTrigger().forJob(weatherDataSyncJobDetail())
                .withIdentity("weatherDataSyncTrigger").withSchedule(schedBuilder).build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory(){
        SchedulerFactoryBean bean = new SchedulerFactoryBean ();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadCount", "20");
        bean.setQuartzProperties(properties);
        bean.setJobDetails(weatherDataSyncJobDetail());
        bean.setTriggers(weatherDataSyncTrigger());
        return bean;
    }
}