package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @description 活动投票详情
 * @author lf
 * @date 2023-08-09
 */
@Data
public class ActivityVoteDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 活动明细表id
     */
    private Long activityDetailId;

    /**
     * 投票人id
     */
    private Long userId;

    /**
     * 投票数
     */
    private Integer voteNum;

    /**
     * 礼物类型
     */
    private Integer giftType;

    /**
     * 投票类型 0 为机器人 1为正常用户
     */
    private Integer voteType;

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