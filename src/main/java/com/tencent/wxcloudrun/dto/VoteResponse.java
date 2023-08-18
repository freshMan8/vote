package com.tencent.wxcloudrun.dto;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Maps;
import com.tencent.wxcloudrun.model.ActivityHeader;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/10 10:13
 */
@Data
public class VoteResponse extends ActivityHeader {

    private String statusDesc;

    private String cateName;

    private String checkNum;

    public VoteResponse(ActivityHeader header) {
        BeanUtil.copyProperties(header,this);
        this.setStatusDesc(new Date().after(header.getEndTime()) ? "已截止" : "进行中");
        Map<String,String> map = Maps.newHashMap();
        map.put("1","节日投票");
        map.put("2","活动评比");
        map.put("3","才艺比拼");
        map.put("4","单位政企");
        map.put("5","其他投票");
        this.setCateName(map.get(this.getActivityType()));
    }
}
