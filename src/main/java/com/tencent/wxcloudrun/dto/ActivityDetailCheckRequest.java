package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/18 15:14
 */
@Data
public class ActivityDetailCheckRequest {

    private Long id;

    private Integer status;

    private String rejectReason;
}
