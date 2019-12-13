package com.learn.quartz.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * quart_task
 * @author 
 */
@Setter
@Getter
@ToString
public class QuartzTask implements Serializable {
    private static final long serialVersionUID = 2605762417952271886L;
    /**
     * 主键
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * group名称
     */
    private String groupName;

    /**
     * job所在类
     */
    private String jobClass;

    /**
     * 参数
     */
    private String params;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 状态
     */
    private Boolean status;

}