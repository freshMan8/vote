package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.contants.ErrorEnum;
import com.tencent.wxcloudrun.dao.ActivityDetailMapper;
import com.tencent.wxcloudrun.dao.ActivityHeaderMapper;
import com.tencent.wxcloudrun.dto.ActivityDetailRequest;
import com.tencent.wxcloudrun.dto.ActivityDetailResponse;
import com.tencent.wxcloudrun.dto.ActivityRequest;
import com.tencent.wxcloudrun.dto.VoteResponse;
import com.tencent.wxcloudrun.exception.VoteExceptionFactory;
import com.tencent.wxcloudrun.model.ActivityDetail;
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

    @Autowired
    private ActivityDetailMapper activityDetailMapper;

    @Override
    public PageInfo<VoteResponse> getList(ActivityRequest request) {
        ActivityHeader param = new ActivityHeader();
        param.setActivityType(request.getSortVal());
        PageInfo<ActivityHeader> pageInfo = PageHelper.startPage(1, 100,"create_time desc")
                .doSelectPageInfo(() -> activityHeaderMapper.pageList(param));
        PageInfo<VoteResponse> dtoPage = new PageInfo<>();
        BeanUtil.copyProperties(pageInfo,dtoPage);
        List<VoteResponse> voteResponses = Lists.newArrayList();
        pageInfo.getList().forEach(s -> voteResponses.add(new VoteResponse(s)));
        dtoPage.setList(voteResponses);
        return dtoPage;
    }

    @Override
    public ActivityDetailResponse getDetailById(ActivityDetailRequest request) {
        ActivityHeader header = activityHeaderMapper.load(request.getId());
        if (header == null) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0006);
        }
        ActivityDetailResponse response = new ActivityDetailResponse();
        BeanUtil.copyProperties(header,response);
        ActivityDetail param = new ActivityDetail();
        param.setActivityId(header.getId());
        List<ActivityDetail> list = activityDetailMapper.pageList(param);
        response.setList(list);
        return response;
    }
}
