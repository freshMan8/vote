package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.ActivityRequest;
import com.tencent.wxcloudrun.model.ActivityHeader;

import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/9 16:53
 */
public interface ActivityHeaderService {

    List<ActivityHeader> getList(ActivityRequest request);
}
