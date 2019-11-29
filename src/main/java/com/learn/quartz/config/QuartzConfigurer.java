package com.learn.quartz.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class QuartzConfigurer {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TaskJobFactory jobFactory;

    @Autowired
    private PlatformTransactionManager txManager;

    @Bean(name = "quartzScheduler")
    public SchedulerFactoryBean quartzScheduler() throws IOException {
        //获取配置属性
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        //创建SchedulerFactoryBean
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(propertiesFactoryBean.getObject());
        //设置job工厂，使job可以自动注入
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
