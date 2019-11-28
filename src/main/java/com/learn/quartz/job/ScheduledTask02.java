package com.learn.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class ScheduledTask02 implements Job {
    /**
     * 日志
     */

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("ScheduledTask => 02 run 当前线程名称 {} ", Thread.currentThread().getName());
    }
}