package com.learn.quartz.service.impl;

import com.learn.quartz.pojo.QuartTask;
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
    Scheduler scheduler;
    /**
     * 程序启动时初始化 ==> 启动所有正常状态的任务
     */
    @Override
    public void initAllTask(List<QuartTask> scheduledTaskBeanList) {
        log.info("程序启动 ==> 初始化所有任务开始 ！size={}", scheduledTaskBeanList.size());
        if (CollectionUtils.isEmpty(scheduledTaskBeanList)) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                log.error("shutdown is fail:", e);
            }
            return;
        }
        for (QuartTask scheduledTask : scheduledTaskBeanList) {
            //任务 key
            TriggerKey triggerKey = TriggerKey.triggerKey(scheduledTask.getName(), scheduledTask.getGroupName());
            boolean result = false;
            try {
                result = scheduler.checkExists(triggerKey);
                log.info("checkExists result = {}", result);
            } catch (SchedulerException e) {
                log.error("checkExists msg:", e);
            }
            //如果存在
            if (result) {
                if (Integer.valueOf(1).equals(scheduledTask.getStatus())) {
                    //update
                    String params = scheduledTask.getMethod();
                    Map<String, Object> map = new HashMap<>();
                    map.put("params", params);
                    this.updateJob(triggerKey, scheduledTask.getCorn(), map);
                } else {
                    //delete
                    this.deleteJob(scheduledTask.getName(), scheduledTask.getGroupName());
                }
            } else {
                //启动任务
                if (Integer.valueOf(1).equals(scheduledTask.getStatus())) {
                    this.addJob(scheduledTask);
                }
            }
        }
        log.info("程序启动 ==> 初始化所有任务结束 ！size={}", scheduledTaskBeanList.size());
    }

    private void addJob(QuartTask scheduledTask) {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(scheduledTask.getJobClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String name = scheduledTask.getName();
        String groupName = scheduledTask.getGroupName();
        String corn = scheduledTask.getCorn();
        String params = scheduledTask.getMethod();
        Map<String, Object> map = new HashMap<>();
        map.put("params", params);
        this.addJob(aClass, name, groupName, corn, map);
    }

    /**
     * 创建job，可传参
     *
     * @param clazz     任务类
     * @param name      任务名称
     * @param groupName 任务所在组名称
     * @param cronExp   cron表达式
     * @param param     map形式参数
     */
    @Override
    public void addJob(Class clazz, String name, String groupName, String cronExp, Map<String, Object> param) {
        try {
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(((Job) clazz.newInstance()).getClass()).withIdentity(name, groupName).build();
            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, groupName).withSchedule(scheduleBuilder).build();
            //获得JobDataMap，写入数据
            if (param != null) {
                trigger.getJobDataMap().putAll(param);
            }
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停job
     *
     * @param name      任务名称
     * @param groupName 任务所在组名称
     */
    @Override
    public void pauseJob(String name, String groupName) {
        try {
            scheduler.pauseJob(JobKey.jobKey(name, groupName));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复job
     *
     * @param name      任务名称
     * @param groupName 任务所在组名称
     */
    @Override
    public void resumeJob(String name, String groupName) {
        try {
            scheduler.resumeJob(JobKey.jobKey(name, groupName));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    /**
     * job 更新,更新频率和参数
     *
     * @param cronExp   cron表达式
     * @param param     参数
     */
    @Override
    public void updateJob(TriggerKey triggerKey, String cronExp, Map<String, Object> param) {
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (cronExp != null) {
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            }

            //修改map
            if (param != null) {
                trigger.getJobDataMap().putAll(param);
            }
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * job 删除
     *
     * @param name      任务名称
     * @param groupName 任务所在组名称
     */
    @Override
    public void deleteJob(String name, String groupName) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(name, groupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(name, groupName));
            scheduler.deleteJob(JobKey.jobKey(name, groupName));
        } catch (Exception e) {
            log.error("deleteJob is fail:", e);
        }
    }


    /**
     * 启动所有定时任务
     */
    @Override
    public void startAllJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭所有定时任务
     */
    @Override
    public void shutdownAllJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
