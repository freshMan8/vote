package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.util.Date;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/8 8:29
 */
@Data
public class News {

    private Long id;

    private String title;

    private String shortContext;

    private String context;

    private String picUrl;

    private Integer sort;

    private Date sortTime;

    private Date updatedTime;

    private String updatedBy;

    private Date createTime;

    private String createBy;
}
