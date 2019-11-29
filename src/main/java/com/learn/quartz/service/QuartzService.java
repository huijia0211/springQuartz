package com.learn.quartz.service;

import com.learn.quartz.pojo.QuartTask;
import org.quartz.TriggerKey;

import java.util.List;
import java.util.Map;

public interface QuartzService {
    void initAllTask(List<QuartTask> scheduledTaskBeanList);

    void addJob(QuartTask scheduledTask);

    /**
     * 添加任务可以传参数
     *
     * @param clazz
     * @param jobName
     * @param groupName
     * @param cronExp
     * @param param
     */
    void addJob(Class clazz, String jobName, String groupName, String cronExp, Map<String, Object> param);


    void updateJob(TriggerKey triggerKey, String cronExp, Map<String, Object> param);

    /**
     * 删除任务
     *
     * @param name
     * @param groupName
     */
    void deleteJob(String name, String groupName);

}
