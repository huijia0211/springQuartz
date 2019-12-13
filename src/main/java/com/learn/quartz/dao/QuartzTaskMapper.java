package com.learn.quartz.dao;

import com.learn.quartz.pojo.QuartzTask;

import java.util.List;

public interface QuartzTaskMapper {

    QuartzTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuartzTask record);

    List<QuartzTask> getAllTask();
}