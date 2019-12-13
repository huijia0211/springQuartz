package com.learn.quartz.job;

import com.learn.quartz.pojo.QuartzTask;
import com.learn.quartz.service.QuartzTaskService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Setter
public class ScheduledTask03 implements Job {

    @Autowired
    private QuartzTaskService quartzTaskService;

    private Integer id;

    private String params;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (quartzTaskService!=null){
            QuartzTask quartzTask = quartzTaskService.selectByPrimaryKey(id);
            log.info("ScheduledTask => 03 quartzTask = {}", quartzTask);
        }
        log.info("ScheduledTask => 03 run 当前线程名称 {}, Data = {}", Thread.currentThread().getName(), params);
    }

}