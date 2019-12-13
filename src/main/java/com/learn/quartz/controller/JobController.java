package com.learn.quartz.controller;

import com.learn.quartz.pojo.QuartzTask;
import com.learn.quartz.service.QuartzService;
import com.learn.quartz.service.QuartzTaskService;
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
    private QuartzService quartzService;

    @Autowired
    private QuartzTaskService quartzTaskService;

    /**
     * 修改任务的cron
     *
     * @return
     */
    @RequestMapping("/updateJobCron")
    public Object updateJobCron(Integer id, String cron) {
        QuartzTask quartzTask = quartzTaskService.selectByPrimaryKey(id);
        Map<String, String> resultMap = new HashMap<>();
        //如果存在quartzTask对象
        if (quartzTask != null) {
            //修改任务的cron
            quartzTask.setCron(cron);
            //更新quart_task
            quartzTaskService.updateByPrimaryKeySelective(quartzTask);
            //如果启用状态则修改当前已持久化的job
            if (quartzTask.getStatus()) {
                quartzService.updateJob(quartzTask);
            }
            resultMap.put("status", "0");
            resultMap.put("msg", "修改cron成功");
            return resultMap;
        }
        //不存在quartzTask对象
        resultMap.put("status", "1");
        resultMap.put("msg", "修改cron失败");
        return resultMap;
    }

    /**
     * 是否启用接口
     *
     * @return
     */
    @RequestMapping("/updateJobStatus")
    public Object updateJobStatus(Integer id, Boolean status) {
        QuartzTask quartzTask = quartzTaskService.selectByPrimaryKey(id);

        Map<String, String> resultMap = new HashMap<>();
        if (quartzTask != null) {
            //修改任务的启用状态
            quartzTask.setStatus(status);
            //更新quart_task
            quartzTaskService.updateByPrimaryKeySelective(quartzTask);
            //根据status判断是新增job还是删除job
            if (status) {
                quartzService.addJob(quartzTask);
            } else {
                quartzService.deleteJob(quartzTask);
            }
            resultMap.put("status", "0");
            resultMap.put("msg", "修改状态成功");
            return resultMap;
        }
        resultMap.put("status", "1");
        resultMap.put("msg", "修改状态失败");
        return resultMap;
    }
}
