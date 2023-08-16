package com.tencent.wxcloudrun.dto;

import com.tencent.wxcloudrun.model.NewsDetail;
import lombok.Data;

import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/15 19:40
 */
@Data
public class UpdateNewsRequest {

    private Long id;

    private String picUrl;

    private List<NewsDetail> contents;

    private String title;

    private String context;

    private Integer sort;
}
