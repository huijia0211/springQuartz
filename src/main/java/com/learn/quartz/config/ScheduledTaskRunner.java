package com.learn.quartz.config;

import com.learn.quartz.dao.QuartzTaskMapper;
import com.learn.quartz.pojo.QuartzTask;
import com.learn.quartz.service.QuartzService;
import com.learn.quartz.service.QuartzTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @see @Order注解的执行优先级是按value值从小到大顺序。
 * 项目启动完毕后开启需要自启的任务
 */
@Component
@Order(value = 1)
@Slf4j
public class ScheduledTaskRunner implements ApplicationRunner {

    @Autowired
    private QuartzTaskService quartzTaskService;

    @Autowired
    private QuartzService quartzService;

    /**
     * 程序启动完毕后,需要自启的任务
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(" >>>>>> 项目启动完毕, 开启 => 需要自启的任务 开始!");
        List<QuartzTask> activatedTaskList = quartzTaskService.getAllTask();
        quartzService.initAllTask(activatedTaskList);
        log.info(" >>>>>> 项目启动完毕, 开启 => 需要自启的任务 结束！");
    }
}
