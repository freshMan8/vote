package com.tencent.wxcloudrun.service;

import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.dto.ActivityDetailCheckRequest;
import com.tencent.wxcloudrun.dto.ActivityDetailInfoResponse;
import com.tencent.wxcloudrun.dto.ActivityDetailRequest;
import com.tencent.wxcloudrun.dto.ActivityDetailResponse;
import com.tencent.wxcloudrun.dto.ActivityRequest;
import com.tencent.wxcloudrun.dto.JoinActivityRequest;
import com.tencent.wxcloudrun.dto.NewsRequest;
import com.tencent.wxcloudrun.dto.NewsStatusRequest;
import com.tencent.wxcloudrun.dto.UpdateActivityDetailRequest;
import com.tencent.wxcloudrun.dto.VoteRequest;
import com.tencent.wxcloudrun.dto.VoteResponse;
import com.tencent.wxcloudrun.model.ActivityContextDetail;
import com.tencent.wxcloudrun.model.ActivityHeader;

import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/9 16:53
 */
public interface ActivityHeaderService {

    PageInfo<VoteResponse> getList(ActivityRequest request);

    ActivityDetailResponse getDetailById(ActivityDetailRequest request);

    void vote(VoteRequest request);

    List<ActivityContextDetail> getContextList(ActivityDetailRequest request);

    PageInfo<VoteResponse> getAdminList(NewsRequest request);

    Integer updateStatus(NewsStatusRequest request);

    ActivityDetailResponse setDetailContent(ActivityDetailResponse response);

    Integer updateActivityDetail(UpdateActivityDetailRequest request);

    void joinActivity(JoinActivityRequest request);

    ActivityDetailInfoResponse getActiveDetail(ActivityDetailRequest request);

    void editActivity(JoinActivityRequest request);

    Integer checkDetail(ActivityDetailCheckRequest request);
}
