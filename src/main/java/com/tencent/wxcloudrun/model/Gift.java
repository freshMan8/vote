package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 礼物详情
 * @author lf
 * @date 2023-08-14
 */
@Data
public class Gift implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 活动类型
     */
    private String activityType;

    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 投票数
     */
    private Integer voteNum;

    /**
     * 价值金额（分）
     */
    private Integer price;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 是否启用
     */
    private Integer enable;

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
