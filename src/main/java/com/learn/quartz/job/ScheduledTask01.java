package com.learn.quartz.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
@Setter
public class ScheduledTask01 implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        log.info("ScheduledTask => 01 run 当前线程名称 {} , Data = {}", Thread.currentThread().getName(), dataMap.getString("params"));
    }
}