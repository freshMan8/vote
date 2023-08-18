package com.tencent.wxcloudrun.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description 活动参与明细表
 * @author lf
 * @date 2023-08-09
 */
@Data
public class ActivityDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 活动主表id
     */
    private Long activityId;

    /**
     * 参与人id
     */
    private Long userId;

    /**
     * 参与人手机号
     */
    private String phoneNum;

    /**
     * 参与人收货地址
     */
    private String location;

    /**
     * 参与人收货人
     */
    private String name;

    /**
     * 主题
     */
    private String title;

    /**
     * 内容缩写
     */
    private String shortContext;

    /**
     * 主题url
     */
    private String picUrl;

    /**
     * 参赛编号
     */
    private Integer orderNum;

    /**
     * 投票数
     */
    private Integer voteNum;

    private Integer status;

    private String rejectReason;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    private List<ActivityContextDetail> content;
}