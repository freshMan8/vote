package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.ActivityHeaderMapper;
import com.tencent.wxcloudrun.dto.ActivityRequest;
import com.tencent.wxcloudrun.model.ActivityHeader;
import com.tencent.wxcloudrun.service.ActivityHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/9 16:53
 */
@Service
public class ActivityHeaderServiceImpl implements ActivityHeaderService {

    @Autowired
    private ActivityHeaderMapper activityHeaderMapper;

    @Override
    public List<ActivityHeader> getList(ActivityRequest request) {
        ActivityHeader param = new ActivityHeader();
        param.setActivityType(request.getActivityType());
        List<ActivityHeader> result = activityHeaderMapper.pageList(param);
        return result;
    }
}
