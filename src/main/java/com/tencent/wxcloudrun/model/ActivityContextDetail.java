package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @description 活动简介
 * @author lf
 * @date 2023-08-10
 */
@Data
public class ActivityContextDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 动态id
     */
    private Long activityId;

    private Long activityDetailId;

    /**
     * 内容
     */
    private String context;

    /**
     * 类型
     */
    private String type;

    /**
     * 排序
     */
    private Integer orders;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

}
