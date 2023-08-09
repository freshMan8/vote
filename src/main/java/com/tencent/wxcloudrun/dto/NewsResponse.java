package com.tencent.wxcloudrun.dto;

import com.tencent.wxcloudrun.model.News;
import lombok.Data;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/9 20:21
 */
@Data
public class NewsResponse extends News {

    private String type = "news";
}
