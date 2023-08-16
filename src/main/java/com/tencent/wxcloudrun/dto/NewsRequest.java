package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/9 15:24
 */
@Data
public class NewsRequest {

    private String search;

    private String sortType;

    private String sortVal;
}
