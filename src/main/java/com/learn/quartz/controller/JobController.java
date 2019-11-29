package com.learn.quartz.controller;

import com.learn.quartz.dao.QuartTaskMapper;
import com.learn.quartz.pojo.QuartTask;
import com.learn.quartz.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class JobController {

    @Autowired
    private QuartzService service;

    @Autowired
    private QuartTaskMapper quartTaskMapper;

    /**
     * 添加新任务
     *
     * @return
     */
    @RequestMapping("/updateJobCron")
    public Object updateJobCron(Integer id, String cron) {
        QuartTask quartTask = quartTaskMapper.selectByPrimaryKey(id);
        quartTask.setCorn(cron);
        quartTaskMapper.updateByPrimaryKeySelective(quartTask);
        String name = quartTask.getName();
        String groupName = quartTask.getGroupName();
        Map<String, String> resultMap = new HashMap<>();
        if (Integer.valueOf(1).equals(quartTask.getStatus())) {
            service.updateJob(quartTask);
        }
        resultMap.put("groupName", groupName);
        resultMap.put("jobName", name);
        return resultMap;
    }

    /**
     * 删除任务
     *
     * @return
     */
    @RequestMapping("/updateJobStatus")
    public Object updateJobStatus(Integer id, Integer status) {
        QuartTask quartTask = quartTaskMapper.selectByPrimaryKey(id);
        quartTask.setStatus(status);
        quartTaskMapper.updateByPrimaryKeySelective(quartTask);
        String name = quartTask.getName();
        String groupName = quartTask.getGroupName();
        if (Integer.valueOf(1).equals(status)) {
            service.addJob(quartTask);
        } else {
            service.deleteJob(quartTask);
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("groupName", groupName);
        resultMap.put("jobName", name);
        return resultMap;
    }
}
