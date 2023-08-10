package com.tencent.wxcloudrun.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 活动主表
 * @author lf
 * @date 2023-08-09
 */
@Data
public class ActivityHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 主题
     */
    private String title;

    /**
     * 主题url
     */
    private String picUrl;

    /**
     * 排序置顶
     */
    private Integer sort;

    /**
     * 置顶过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sortTime;

    /**
     * 参与人数
     */
    private Integer participantNum;

    /**
     * 投票数
     */
    private Integer voteNum;

    /**
     * 访问数
     */
    private Integer visitNum;

    /**
     * 活动类型
     */
    private String activityType;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

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
}