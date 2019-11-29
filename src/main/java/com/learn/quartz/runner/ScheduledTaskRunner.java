package com.learn.quartz.runner;

import com.learn.quartz.dao.QuartTaskMapper;
import com.learn.quartz.pojo.QuartTask;
import com.learn.quartz.service.QuartzService;
import com.learn.quartz.service.ScheduledTaskService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * @see @Order注解的执行优先级是按value值从小到大顺序。
 * 项目启动完毕后开启需要自启的任务
 */
@Component
@Order(value = 1)
@Slf4j
public class ScheduledTaskRunner implements ApplicationRunner {


    @Autowired
    private QuartTaskMapper taskMapper;

    @Autowired
    private QuartzService quartzService;

    /**
     * 程序启动完毕后,需要自启的任务
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(" >>>>>> 项目启动完毕, 开启 => 需要自启的任务 开始!");
        List<QuartTask> activatedTaskList = taskMapper.getAllTask();
        quartzService.initAllTask(activatedTaskList);
        log.info(" >>>>>> 项目启动完毕, 开启 => 需要自启的任务 结束！");
    }
}
