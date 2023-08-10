package com.tencent.wxcloudrun.dto;

import com.tencent.wxcloudrun.model.ActivityDetail;
import com.tencent.wxcloudrun.model.ActivityHeader;
import lombok.Data;

import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/10 11:17
 */
@Data
public class ActivityDetailResponse extends ActivityHeader {

    private List<ActivityDetail> list;
}
