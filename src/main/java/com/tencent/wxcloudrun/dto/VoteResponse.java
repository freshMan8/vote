package com.tencent.wxcloudrun.dto;

import cn.hutool.core.bean.BeanUtil;
import com.tencent.wxcloudrun.model.ActivityHeader;
import lombok.Data;

import java.util.Date;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/10 10:13
 */
@Data
public class VoteResponse extends ActivityHeader {

    private String statusDesc;

    public VoteResponse(ActivityHeader header) {
        BeanUtil.copyProperties(header,this);
        this.setStatusDesc(new Date().after(header.getEndTime()) ? "已截止" : "进行中");
    }
}
