package com.learn.quartz.job;

import com.learn.quartz.dao.QuartTaskMapper;
import com.learn.quartz.pojo.QuartTask;
import com.learn.quartz.service.QuartzService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
@Setter
public class ScheduledTask02 implements Job {

    @Autowired
    private QuartTaskMapper mapper;

    private String params;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        if (mapper!=null){
            List<QuartTask> allTask = mapper.getAllTask();
            log.info("allTask = {}", allTask);
        }
        log.info("ScheduledTask => 02 run 当前线程名称 {}, Data = {}", Thread.currentThread().getName(), params);
    }
}