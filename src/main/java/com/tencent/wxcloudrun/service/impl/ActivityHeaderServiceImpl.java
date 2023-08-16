package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.contants.ErrorEnum;
import com.tencent.wxcloudrun.dao.ActivityContextDetailMapper;
import com.tencent.wxcloudrun.dao.ActivityDetailMapper;
import com.tencent.wxcloudrun.dao.ActivityHeaderMapper;
import com.tencent.wxcloudrun.dao.ActivityVoteDetailMapper;
import com.tencent.wxcloudrun.dto.ActivityDetailRequest;
import com.tencent.wxcloudrun.dto.ActivityDetailResponse;
import com.tencent.wxcloudrun.dto.ActivityRequest;
import com.tencent.wxcloudrun.dto.NewsRequest;
import com.tencent.wxcloudrun.dto.NewsStatusRequest;
import com.tencent.wxcloudrun.dto.VoteRequest;
import com.tencent.wxcloudrun.dto.VoteResponse;
import com.tencent.wxcloudrun.exception.VoteExceptionFactory;
import com.tencent.wxcloudrun.model.ActivityContextDetail;
import com.tencent.wxcloudrun.model.ActivityDetail;
import com.tencent.wxcloudrun.model.ActivityHeader;
import com.tencent.wxcloudrun.model.ActivityVoteDetail;
import com.tencent.wxcloudrun.redis.RedissonLockService;
import com.tencent.wxcloudrun.service.ActivityHeaderService;
import com.tencent.wxcloudrun.util.VoteContext;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private ActivityContextDetailMapper activityContextDetailMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Resource(name = "refershExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ActivityVoteDetailMapper voteDetailMapper;

    @Override
    public PageInfo<VoteResponse> getList(ActivityRequest request) {
        ActivityHeader param = new ActivityHeader();
        param.setActivityType(request.getSortVal());
        param.setEnable(1);
        PageInfo<ActivityHeader> pageInfo = PageHelper.startPage(1, 1000,"sort asc")
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
        ActivityContextDetail detailParam = new ActivityContextDetail();
        detailParam.setActivityId(request.getId());
        List<ActivityContextDetail> details = activityContextDetailMapper.pageList(detailParam);
        response.setContextDetails(details);
        refreshVistNum(request.getId());
        return response;
    }

    @Override
    public void vote(VoteRequest request) {
        checkTodayVotes(request);
    }

    @Override
    public List<ActivityContextDetail> getContextList(ActivityDetailRequest request) {
        ActivityContextDetail activityContextDetail = new ActivityContextDetail();
        activityContextDetail.setActivityDetailId(request.getId());
        List<ActivityContextDetail> list = activityContextDetailMapper.pageList(activityContextDetail);
        return list;
    }

    @Override
    public PageInfo<VoteResponse> getAdminList(NewsRequest request) {
        ActivityHeader param = new ActivityHeader();
        param.setTitle(request.getSearch());
        if ("status".equals(request.getSortType()) && "1".equals(request.getSortVal())) {
            param.setEnable(1);
        } else if ("status".equals(request.getSortType()) && "0".equals(request.getSortVal())) {
            param.setEnable(0);
        }
        PageInfo<ActivityHeader> pageInfo = PageHelper.startPage(1, 1000,"sort asc")
                .doSelectPageInfo(() -> activityHeaderMapper.pageList(param));
        PageInfo<VoteResponse> dtoPage = new PageInfo<>();
        BeanUtil.copyProperties(pageInfo,dtoPage);
        List<VoteResponse> voteResponses = Lists.newArrayList();
        pageInfo.getList().forEach(s -> voteResponses.add(new VoteResponse(s)));
        dtoPage.setList(voteResponses);
        return dtoPage;
    }

    @Override
    public Integer updateStatus(NewsStatusRequest request) {
        ActivityHeader param = new ActivityHeader();
        param.setEnable(request.getStatus());
        param.setSort(request.getSort());
        param.setId(request.getId());
        param.setUpdatedBy("admin");
        return activityHeaderMapper.update(param);
    }

    public void refreshPersonNum(ActivityDetail activityDetail,Integer num,Long userId) {
        RLock lock = null;
        try {
            lock = RedissonLockService.getAndTryLock(RedissonLockService.getLockKey("refresh", activityDetail.getActivityId() + ""));
            ActivityVoteDetail voteDetail = new ActivityVoteDetail();
            voteDetail.setVoteNum(num);
            voteDetail.setActivityDetailId(activityDetail.getId());
            voteDetail.setGiftType(0);
            voteDetail.setVoteType(1);
            voteDetail.setUserId(userId);
            voteDetail.setCreateBy("admin");
            voteDetail.setUpdatedBy("admin");
            voteDetailMapper.insert(voteDetail);

            List<ActivityVoteDetail> list = voteDetailMapper.getListByActiveId(activityDetail.getActivityId());
            ActivityHeader updateParam = new ActivityHeader();
            updateParam.setId(activityDetail.getActivityId());
            updateParam.setParticipantNum((int)list.stream().map(ActivityVoteDetail::getUserId).distinct().count());
            activityHeaderMapper.update(updateParam);
        } finally {
            RedissonLockService.unlockDirectly(lock);
        }
    }

    public void refreshVistNum(Long headerId) {
        RLock lock = null;
        try {
            lock = RedissonLockService.getAndTryLock(RedissonLockService.getLockKey("refresh",headerId + ""));
            ActivityHeader header = activityHeaderMapper.load(headerId);
            ActivityHeader updateParam = new ActivityHeader();
            updateParam.setId(headerId);
            updateParam.setVisitNum(header.getVisitNum() + 1);
            activityHeaderMapper.update(updateParam);
        } finally {
            RedissonLockService.unlockDirectly(lock);
        }
    }

    public void refreshVoteNum(Long headerId) {
        RLock lock = null;
        try {
            lock = RedissonLockService.getAndTryLock(RedissonLockService.getLockKey("refresh", headerId + ""));
            ActivityDetail detail = new ActivityDetail();
            detail.setActivityId(headerId);
            List<ActivityDetail> details = activityDetailMapper.pageList(detail);
            Integer integer = details.stream().map(ActivityDetail::getVoteNum).reduce(Integer::sum).orElse(0);
            ActivityHeader update = new ActivityHeader();
            update.setVoteNum(integer);
            update.setParticipantNum((int)details.stream().map(ActivityDetail::getPhoneNum).distinct().count());
            update.setId(headerId);
            activityHeaderMapper.update(update);
        } finally {
            RedissonLockService.unlockDirectly(lock);
        }
    }

    private void checkTodayVotes(VoteRequest request) {
        String phoneNum = VoteContext.session().getPhoneNum();
        Long userId = VoteContext.session().getId();
        String lockKey = RedissonLockService.getLockKey("today_check",phoneNum);
        RLock todayLock = null;
        RLock detailLock = null;
        try {
            todayLock = RedissonLockService.getAndTryLock(lockKey);
            if (todayLock == null) {
                throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0008);
            }
            String voteLock = RedissonLockService.getLockKey("vote",request.getId() + "");
            detailLock = RedissonLockService.getAndTryLock(voteLock);
            if (detailLock == null) {
                throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0008);
            }
            ActivityDetail activityDetail = activityDetailMapper.load(request.getId());
            if (activityDetail == null) {
                throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0006);
            }
            ActivityHeader activityHeader = activityHeaderMapper.load(activityDetail.getActivityId());
            if (activityHeader == null) {
                throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0006);
            }
            if (activityHeader.getEndTime().before(new Date())) {
                throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0010);
            }
            String textKey = RedissonLockService.getLockKey("today",request.getVoteId() + "",phoneNum);
            RAtomicLong atomicLong = redissonClient.getAtomicLong(textKey);
            if (atomicLong.isExists() && atomicLong.get() >= activityHeader.getVoteLimit()) {
                throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0009);
            }
            ActivityDetail updateParam = new ActivityDetail();
            updateParam.setId(activityDetail.getId());
            updateParam.setVoteNum(activityDetail.getVoteNum() + 1);
            activityDetailMapper.update(updateParam);
            if (!atomicLong.isExists()) {
                atomicLong.set(1L);
                if (activityHeader.getVoteType() == 1) {
                    LocalTime midnight = LocalTime.MIDNIGHT;
                    LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
                    LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
                    LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
                    long time = TimeUnit.NANOSECONDS.toSeconds(Duration.between(LocalDateTime.now(), tomorrowMidnight).toNanos());
                    atomicLong.expire(time, TimeUnit.SECONDS);
                }
            } else {
                atomicLong.incrementAndGet();
            }
            threadPoolTaskExecutor.submit(() -> {
                refreshVoteNum(activityHeader.getId());
                refreshPersonNum(activityDetail,1,userId);
            });
        } finally {
            RedissonLockService.unlockDirectly(todayLock);
            RedissonLockService.unlockDirectly(detailLock);
        }
    }
}
