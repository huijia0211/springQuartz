package com.learn.quartz.service.impl;

import com.learn.quartz.pojo.QuartTask;
import com.learn.quartz.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 程序启动时初始化 ==> 启动所有正常状态的任务
     */
    @Override
    public void initAllTask(List<QuartTask> quartTaskList) throws Exception {
        log.info("程序启动 ==> 初始化所有任务开始 ！");
        if (CollectionUtils.isEmpty(quartTaskList)) {
            scheduler.shutdown();
            return;
        }
        for (QuartTask quartTask : quartTaskList) {
            //判断是否启动状态
            if (Integer.valueOf(1).equals(quartTask.getStatus())) {
                this.addJob(quartTask);
            }
        }
        log.info("程序启动 ==> 初始化所有任务结束 ！");
    }

    /**
     * 创建job，可传参
     *
     * @param quartTask 任务名称
     */
    @Override
    public void addJob(QuartTask quartTask) {
        String name = quartTask.getName();
        String groupName = quartTask.getGroupName();
        String corn = quartTask.getCorn();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("params", quartTask.getMethod());
        TriggerKey triggerKey = TriggerKey.triggerKey(name, groupName);
        try {
            boolean result = scheduler.checkExists(triggerKey);
            //job已存在，直接返回
            log.info("checkExists quartTask = {} , result = {}", quartTask, result);
            if (result) {
                return;
            }
            Class<?> aClass = Class.forName(quartTask.getJobClass());
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(((Job) aClass.newInstance()).getClass()).withIdentity(name, groupName).build();
            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(corn);
            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().startAt(new Date()).withIdentity(name, groupName).withSchedule(scheduleBuilder).build();
            //获得JobDataMap，写入数据
            trigger.getJobDataMap().putAll(paramMap);
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("addJob quartTask = {} is success", quartTask);
        } catch (Exception e) {
            log.error("addJob quartTask = {} is fail, msg = {}", quartTask, e);
        }
    }


    /**
     * job 更新,更新频率和参数
     *
     * @param quartTask 任务名称
     */
    @Override
    public void updateJob(QuartTask quartTask) {
        String name = quartTask.getName();
        String groupName = quartTask.getGroupName();
        String cron = quartTask.getCorn();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("params", quartTask.getMethod());
        TriggerKey triggerKey = TriggerKey.triggerKey(name, groupName);
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (cron != null) {
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().startAt(new Date()).withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            }
            //修改map
            trigger.getJobDataMap().putAll(paramMap);
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info("updateJob quartTask = {} is success", quartTask);
        } catch (SchedulerException e) {
            log.error("updateJob quartTask = {} is fail, msg = {}", quartTask, e);
        }
    }

    /**
     * job 删除
     *
     * @param quartTask 任务名称
     */
    @Override
    public void deleteJob(QuartTask quartTask) {
        String name = quartTask.getName();
        String groupName = quartTask.getGroupName();
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(name, groupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(name, groupName));
            scheduler.deleteJob(JobKey.jobKey(name, groupName));
            log.info("deleteJob quartTask = {} is success", quartTask);
        } catch (SchedulerException e) {
            log.error("deleteJob quartTask = {} is fail, msg = {}", quartTask, e);
        }
    }

}
