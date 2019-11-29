package com.learn.quartz.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * quart_task
 * @author 
 */
@Setter
@Getter
@ToString
public class QuartTask implements Serializable {
    private Integer id;

    private String name;

    private String groupName;

    private String jobClass;

    private String method;

    private String corn;

    private Integer status;

    private static final long serialVersionUID = 1L;

}