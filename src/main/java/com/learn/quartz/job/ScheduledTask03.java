package com.learn.quartz.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
@Setter
public class ScheduledTask03 implements Job {

    private String params;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("ScheduledTask => 03 run 当前线程名称 {}, Data = {}", Thread.currentThread().getName(), params);
    }
}