package com.learn.quartz.controller;

import com.learn.quartz.dao.QuartTaskMapper;
import com.learn.quartz.pojo.QuartTask;
import com.learn.quartz.service.QuartzService;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
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
    @RequestMapping("/addJob")
    public Object updateJobCron(Integer id, String cron) {
        QuartTask quartTask = quartTaskMapper.selectByPrimaryKey(id);
        quartTask.setCorn(cron);
        quartTaskMapper.updateByPrimaryKeySelective(quartTask);
        String name = quartTask.getName();
        String groupName = quartTask.getGroupName();
        TriggerKey triggerKey = TriggerKey.triggerKey(name, groupName);
        if (Integer.valueOf(1).equals(quartTask.getStatus())) {
            service.updateJob(triggerKey, cron, null);
        }
        Map<String, String> resultMap = new HashMap<>();
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
            service.deleteJob(name, groupName);
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("groupName", groupName);
        resultMap.put("jobName", name);
        return resultMap;
    }    
}
