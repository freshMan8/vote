package com.tencent.wxcloudrun.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tencent.wxcloudrun.model.NewsDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/10 8:29
 */
@Data
public class NewsDetailResponse {

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date newsDate;

    private String newsCate;

    private List<NewsDetail> content;
}
