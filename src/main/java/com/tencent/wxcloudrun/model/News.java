package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @description 动态
 * @author lf
 * @date 2023-08-09
 */
@Data
public class News implements Serializable {

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
     * 内容缩写
     */
    private String shortContext;

    /**
     * 内容
     */
    private String context;

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
    private Date sortTime;

    private String type;

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