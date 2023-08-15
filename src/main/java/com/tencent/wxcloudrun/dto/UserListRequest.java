package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/15 14:29
 */
@Data
public class UserListRequest {

    private String sortType;

    private String sortVal;

    private String search;
}
