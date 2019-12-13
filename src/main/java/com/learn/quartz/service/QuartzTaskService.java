package com.learn.quartz.service;

import com.learn.quartz.pojo.QuartzTask;

import java.util.List;

public interface QuartzTaskService {

    QuartzTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuartzTask record);

    List<QuartzTask> getAllTask();
}
