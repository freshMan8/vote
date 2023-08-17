package com.tencent.wxcloudrun.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.model.ActivityDetail;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/16 15:37
 */
@Data
public class UpdateActivityDetailRequest {

    private Long id;

    private String title;

    private String cateId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;

    private Integer maxCnt;

    private Integer order;

    private Integer type;

    private Integer theme;

    private List<FromsInfo> forms = Lists.newArrayList();

    private List<ActivityDetail> item = Lists.newArrayList();

    @Data
    public static class FromsInfo {
        private String mark;

        private String title;

        private String type;

        private List<Object> val;
    }
}
