package com.learn.quartz.service.impl;

import com.learn.quartz.pojo.QuartzTask;
import com.learn.quartz.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    public void initAllTask(List<QuartzTask> quartzTaskList) throws Exception {
        log.info("程序启动 ==> 初始化所有任务开始 ！");
        if (CollectionUtils.isEmpty(quartzTaskList)) {
            scheduler.shutdown();
            return;
        }
        for (QuartzTask quartzTask : quartzTaskList) {
            //判断是否启动状态
            if (quartzTask.getStatus()) {
                this.addJob(quartzTask);
            }
        }
        log.info("程序启动 ==> 初始化所有任务结束 ！");
    }

    /**
     * 创建job，可传参
     *
     * @param quartzTask 任务名称
     */
    @Override
    public void addJob(QuartzTask quartzTask) {
        String taskName = quartzTask.getTaskName();
        String groupName = quartzTask.getGroupName();
        String cron = quartzTask.getCron();
        TriggerKey triggerKey = TriggerKey.triggerKey(taskName, groupName);
        try {
            boolean result = scheduler.checkExists(triggerKey);
            //job已存在，直接返回
            log.info("checkExists quartTask = {} , result = {}", quartzTask, result);
            if (result) {
                return;
            }
            Class<?> aClass = Class.forName(quartzTask.getJobClass());
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(((Job) aClass.newInstance()).getClass()).withIdentity(taskName, groupName).build();
            //表达式调度构建器(即任务执行的时间)
            /*使用withMisfireHandlingInstructionDoNothing是因为重启项目时之前添加的job都算失火
            ，默认配置withMisfireHandlingInstructionFireAndProceed失火后会立即执行一遍，
            而withMisfireHandlingInstructionDoNothing失火后不会立即执行，而是按照下一个cron执行
            */
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().startNow().withIdentity(taskName, groupName).withSchedule(scheduleBuilder).build();
            //获得JobDataMap，写入数据
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("id", quartzTask.getId());
            paramMap.put("params", quartzTask.getParams());
            trigger.getJobDataMap().putAll(paramMap);
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("addJob quartTask = {} is success", quartzTask);
        } catch (Exception e) {
            log.error("addJob quartTask = {} is fail, msg = {}", quartzTask, e);
        }
    }


    /**
     * job 更新,更新频率和参数
     *
     * @param quartzTask 任务名称
     */
    @Override
    public void updateJob(QuartzTask quartzTask) {
        String taskName = quartzTask.getTaskName();
        String groupName = quartzTask.getGroupName();
        String cron = quartzTask.getCron();
        TriggerKey triggerKey = TriggerKey.triggerKey(taskName, groupName);
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (cron != null) {
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().startNow().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            }
            //获得JobDataMap，写入数据
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("id", quartzTask.getId());
            paramMap.put("params", quartzTask.getParams());
            trigger.getJobDataMap().putAll(paramMap);
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info("updateJob quartTask = {} is success", quartzTask);
        } catch (SchedulerException e) {
            log.error("updateJob quartTask = {} is fail, msg = {}", quartzTask, e);
        }
    }

    /**
     * job 删除
     *
     * @param quartzTask 任务名称
     */
    @Override
    public void deleteJob(QuartzTask quartzTask) {
        String taskName = quartzTask.getTaskName();
        String groupName = quartzTask.getGroupName();
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(taskName, groupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(taskName, groupName));
            scheduler.deleteJob(JobKey.jobKey(taskName, groupName));
            log.info("deleteJob quartTask = {} is success", quartzTask);
        } catch (SchedulerException e) {
            log.error("deleteJob quartTask = {} is fail, msg = {}", quartzTask, e);
        }
    }
}
