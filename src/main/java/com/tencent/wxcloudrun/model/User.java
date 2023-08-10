package com.tencent.wxcloudrun.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户
 *
 * @author 3832
 * @date 2023/8/2 9:59
 */
@Data
public class User {

    private Long id;

    private String account;

    private String userName;

    private String phoneNum;

    private String picUrl;

    private Integer userType;

    private String updatedBy;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
