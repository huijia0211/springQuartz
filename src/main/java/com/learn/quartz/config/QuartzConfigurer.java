package com.learn.quartz.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class QuartzConfigurer {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager txManager;

    @Bean(name = "quartzScheduler")
    public SchedulerFactoryBean quartzScheduler() throws IOException {
        //创建SchedulerFactoryBean
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //设置调度器自动运行
        factory.setAutoStartup(true);
        //设置配置文件位置
        factory.setConfigLocation(new ClassPathResource("/quartz.properties"));
        //设置job工厂，使job可以自动注入
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        factory.setJobFactory(jobFactory);
        //设置数据源
        factory.setDataSource(dataSource);
        //设置事务
        factory.setTransactionManager(txManager);
        //设置重写已存在的Job
        factory.setOverwriteExistingJobs(true);
        return factory;
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() throws IOException {
        return quartzScheduler().getScheduler();
    }
}
