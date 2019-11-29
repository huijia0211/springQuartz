package com.learn.quartz.job;

import com.learn.quartz.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class ScheduledTask02 implements Job {

    @Autowired
    private QuartzService service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        if (service!=null){
            log.info("111111111");
        }
        log.info("ScheduledTask => 02 run 当前线程名称 {}, Data = {}", Thread.currentThread().getName(), dataMap.getString("params"));
    }
}