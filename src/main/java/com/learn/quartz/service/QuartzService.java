package com.learn.quartz.service;

import com.learn.quartz.pojo.QuartTask;

import java.util.List;

public interface QuartzService {
    void initAllTask(List<QuartTask> scheduledTaskBeanList) throws Exception;

    void addJob(QuartTask scheduledTask);

    void updateJob(QuartTask quartTask);

    void deleteJob(QuartTask quartTask);

}
