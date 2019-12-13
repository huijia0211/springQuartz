package com.learn.quartz.service.impl;

import com.learn.quartz.dao.QuartzTaskMapper;
import com.learn.quartz.pojo.QuartzTask;
import com.learn.quartz.service.QuartzTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartzTaskServiceImpl implements QuartzTaskService {

    @Autowired
    private QuartzTaskMapper quartzTaskMapper;

    @Override
    public QuartzTask selectByPrimaryKey(Integer id) {
        return quartzTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(QuartzTask record) {
        return quartzTaskMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<QuartzTask> getAllTask() {
        return quartzTaskMapper.getAllTask();
    }

}
