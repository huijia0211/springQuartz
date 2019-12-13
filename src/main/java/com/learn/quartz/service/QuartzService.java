package com.learn.quartz.service;

import com.learn.quartz.pojo.QuartzTask;

import java.util.List;

public interface QuartzService {
    void initAllTask(List<QuartzTask> scheduledTaskBeanList) throws Exception;

    void addJob(QuartzTask scheduledTask);

    void updateJob(QuartzTask quartzTask);

    void deleteJob(QuartzTask quartzTask);

}
