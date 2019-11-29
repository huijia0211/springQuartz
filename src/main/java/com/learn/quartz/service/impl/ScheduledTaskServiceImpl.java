//package com.learn.quartz.service.impl;
//
//import com.learn.quartz.dao.QuartTaskMapper;
//import com.learn.quartz.pojo.QuartTask;
//import com.learn.quartz.service.ScheduledTaskService;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * 定时任务实现
// */
//@Service
//@Slf4j
//public class ScheduledTaskServiceImpl implements ScheduledTaskService {
//
//    @Autowired
//    private QuartTaskMapper taskMapper;
//    /**
//     * 可重入锁
//     */
//    private ReentrantLock lock = new ReentrantLock();
//
//
//    private Scheduler sched;
//    /**
//     * 所有定时任务存放Map
//     * key :任务 key
//     * value:任务实现
//     */
//    @Autowired
//    @Qualifier(value = "scheduledTaskJobMap")
//    private Map<String, Job> scheduledTaskJobMap;
//
//    /**
//     * 所有任务列表
//     */
//    @Override
//    public List<QuartTask> taskList() {
//        log.info(">>>>>> 获取任务列表开始 >>>>>> ");
////        //数据库查询所有任务 => 未做分页
////        List<QuartTask> taskBeanList = taskMapper.getAllTask();
////        if (CollectionUtils.isEmpty(taskBeanList)) {
////            return new ArrayList<>();
////        }
////
////        for (QuartTask taskBean : taskBeanList) {
////            String taskKey = taskBean.getId().toString();
////            //是否启动标记处理
////            taskBean.setStatus(this.isStart(taskKey));
////        }
////        log.info(">>>>>> 获取任务列表结束 >>>>>> ");
//        return null;
//    }
//
//
//    /**
//     * 根据任务key 启动任务
//     */
//    @Override
//    public Boolean start(String taskKey) {
//        log.info(">>>>>> 启动任务 {} 开始 >>>>>>", taskKey);
//        //添加锁放一个线程启动，防止多人启动多次
//        lock.lock();
//        log.info(">>>>>> 添加任务启动锁完毕");
//        try {
//            //校验是否已经启动
//            if (this.isStart(taskKey)) {
//                log.info(">>>>>> 当前任务已经启动，无需重复启动！");
//                return false;
//            }
//            //校验任务是否存在
//            if (!scheduledTaskJobMap.containsKey(taskKey)) {
//                return false;
//            }
//            //根据key数据库获取任务配置信息
//            QuartTask scheduledTask = taskMapper.selectByPrimaryKey(Integer.parseInt(taskKey));
//            //启动任务
//            this.doStartTask(scheduledTask);
//        } finally {
//            // 释放锁
//            lock.unlock();
//            log.info(">>>>>> 释放任务启动锁完毕");
//        }
//        log.info(">>>>>> 启动任务 {} 结束 >>>>>>", taskKey);
//        return true;
//    }
//
//    /**
//     * 根据 key 停止任务
//     */
//    @Override
//    public Boolean stop(String taskKey) {
////        log.info(">>>>>> 进入停止任务 {} >>>>>>", taskKey);
////        //当前任务实例是否存在
////        boolean taskStartFlag = scheduledFutureMap.containsKey(taskKey);
////        log.info(">>>>>> 当前任务实例是否存在 {}", taskStartFlag);
////        if (taskStartFlag) {
////            //获取任务实例
////            ScheduledFuture scheduledFuture = scheduledFutureMap.get(taskKey);
////            //关闭实例
////            scheduledFuture.cancel(true);
////        }
////        log.info(">>>>>> 结束停止任务 {} >>>>>>", taskKey);
//        return null;
//    }
//
//    /**
//     * 根据任务key 重启任务
//     */
//    @Override
//    public Boolean restart(String taskKey) {
//        log.info(">>>>>> 进入重启任务 {} >>>>>>", taskKey);
//        //先停止
//        this.stop(taskKey);
//        //再启动
//        return this.start(taskKey);
//    }
//
//    /**
//     * 程序启动时初始化 ==> 启动所有正常状态的任务
//     */
//    @Override
//    public void initAllTask(List<QuartTask> scheduledTaskBeanList) {
//        log.info("程序启动 ==> 初始化所有任务开始 ！size={}", scheduledTaskBeanList.size());
//        if (CollectionUtils.isEmpty(scheduledTaskBeanList)) {
//            return;
//        }
//        for (QuartTask scheduledTask : scheduledTaskBeanList) {
//        //任务 key
//            String taskKey = scheduledTask.getId().toString();
//        //校验是否已经启动
//            if (this.isStart(taskKey)) {
//                continue;
//            }
//        //启动任务
//            this.doStartTask(scheduledTask);
//        }
//        log.info("程序启动 ==> 初始化所有任务结束 ！size={}", scheduledTaskBeanList.size());
//    }
//
//    /**
//     * 执行启动任务
//     */
//    @Override
//    @SuppressWarnings("unchecked")
//    public void doStartTask(QuartTask scheduledTask) {
//        //任务key
//        String taskKey = scheduledTask.getId().toString();
//        //定时表达式
//        String taskCron = scheduledTask.getCorn();
//        try {
//        //获取需要定时调度的接口
//        log.info(">>>>>> 任务 [ {} ] ,cron={}", scheduledTask.getMethod(), taskCron);
//        JobDetail job = JobBuilder.newJob((Class<? extends Job>) Class.forName(scheduledTask.getJobClass()))
//                .withIdentity(JobKey.jobKey(taskKey))
//                .build();
//
//        CronTrigger trigger = TriggerBuilder.newTrigger()
//                .withIdentity(TriggerKey.triggerKey(taskKey))
//                .withSchedule(CronScheduleBuilder.cronSchedule(taskCron)).build();
//        sched.scheduleJob(job, trigger);
//
//        } catch (Exception e) {
//            log.info("name = {} is fail", scheduledTask.getName());
//        }
//    }
//
//    /**
//     * 任务是否已经启动
//     */
//    private Boolean isStart(String taskKey) {
//        boolean result = false;
//        try {
//            result = sched.checkExists(TriggerKey.triggerKey(taskKey));
//            log.info("taskKey isStart result = {}", result);
//        } catch (SchedulerException e) {
//            log.error("msg:", e);
//        }
//        return result;
//    }
//
//}