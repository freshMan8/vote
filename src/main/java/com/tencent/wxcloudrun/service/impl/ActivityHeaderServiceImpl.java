package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.contants.ErrorEnum;
import com.tencent.wxcloudrun.dao.ActivityContextDetailMapper;
import com.tencent.wxcloudrun.dao.ActivityDetailMapper;
import com.tencent.wxcloudrun.dao.ActivityHeaderMapper;
import com.tencent.wxcloudrun.dao.ActivityVoteDetailMapper;
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
import com.tencent.wxcloudrun.exception.VoteExceptionFactory;
import com.tencent.wxcloudrun.model.ActivityContextDetail;
import com.tencent.wxcloudrun.model.ActivityDetail;
import com.tencent.wxcloudrun.model.ActivityHeader;
import com.tencent.wxcloudrun.model.ActivityVoteDetail;
import com.tencent.wxcloudrun.model.AuthSession;
import com.tencent.wxcloudrun.redis.RedissonLockService;
import com.tencent.wxcloudrun.service.ActivityHeaderService;
import com.tencent.wxcloudrun.util.VoteContext;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    @Resource(name = "refreshExecutor")
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
        pageInfo.getList().forEach(s -> {
            VoteResponse response = new VoteResponse(s);
            response.setParticipantNum(activityDetailMapper.getNumByActiveId(s.getId()));
            voteResponses.add(response);
        });
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
        if (request.getStatus() == null) {
            param.setStatus(1);
        }
        List<ActivityDetail> list = activityDetailMapper.pageList(param);
        response.setList(list);
        ActivityContextDetail detailParam = new ActivityContextDetail();
        detailParam.setActivityId(request.getId());
        List<ActivityContextDetail> details = activityContextDetailMapper.pageList(detailParam);
        response.setContextDetails(details);
        refreshVistNum(request.getId());
        response.setCurrentUserJoined(0);
        AuthSession session = VoteContext.session();
        if (session != null && session.getId() != null && session.getId() > 0) {
            ActivityDetail param1 = new ActivityDetail();
            param1.setActivityId(header.getId());
            param1.setUserId(session.getId());
            if (activityDetailMapper.pageList(param1).size() > 0) {
                response.setCurrentUserJoined(1);
            }
        }
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
        if ("cateId".equals(request.getSortType())) {
            param.setActivityType(request.getSortVal());
        }
        String sort = "sort asc";
        if ("sort".equals(request.getSortType())) {
            switch (request.getSortVal()) {
                case "user" : sort = "visit_num desc";break;
                case "cnt" : sort = "vote_num desc";break;
                case "start" : sort = "start_time asc";break;
                case "end" : sort = "end_time asc";break;
                case "new" : sort = "create_time desc";break;
                default:sort ="sort asc";
            }
        }
        PageInfo<ActivityHeader> pageInfo = PageHelper.startPage(1, 1000,sort)
                .doSelectPageInfo(() -> activityHeaderMapper.pageList(param));
        PageInfo<VoteResponse> dtoPage = new PageInfo<>();
        BeanUtil.copyProperties(pageInfo,dtoPage);
        List<VoteResponse> voteResponses = Lists.newArrayList();
        pageInfo.getList().forEach(s -> {
            VoteResponse response = new VoteResponse(s);
            response.setCheckNum(activityDetailMapper.getNumCheckByActiveId(s.getId()));
            voteResponses.add(response);
        });
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityDetailResponse setDetailContent(ActivityDetailResponse response) {
        List<ActivityDetail> details = response.getList();
        if (!CollectionUtils.isEmpty(details)) {
            for (ActivityDetail detail : details) {
                ActivityDetailRequest req = new ActivityDetailRequest();
                req.setId(detail.getId());
                List<ActivityContextDetail> list = getContextList(req);
                detail.setContent(list);
            }
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateActivityDetail(UpdateActivityDetailRequest request) {
        Map<String,List<Object>> map = request.getForms().stream()
                .collect(Collectors.toMap(UpdateActivityDetailRequest.FromsInfo::getMark,
                        UpdateActivityDetailRequest.FromsInfo::getVal));
        ActivityHeader activityHeader = new ActivityHeader();
        activityHeader.setActivityType(request.getCateId());
        activityHeader.setSort(request.getOrder());
        activityHeader.setUpdatedBy("admin");
        // activityHeader.setEnable(1);
        activityHeader.setVoteThyme(request.getTheme());
        activityHeader.setTitle(request.getTitle());
        activityHeader.setId(request.getId());
        activityHeader.setPicUrl(map.get("cover").get(0).toString());
        activityHeader.setStartTime(request.getStart());
        activityHeader.setEndTime(request.getEnd());
        activityHeader.setVoteType(request.getType());
        activityHeader.setVoteLimit(request.getMaxCnt());
        if (request.getId() != null) {
            activityHeader.setUpdatedBy("admin");
            activityHeaderMapper.update(activityHeader);
        } else {
            activityHeader.setEnable(1);
            activityHeader.setCreateBy("admin");
            activityHeader.setUpdatedBy("admin");
            activityHeaderMapper.insert(activityHeader);
        }
        // activityDetailMapper.delete(activityHeader.getId());
        int order = 1;
        ActivityDetail detailParam = new ActivityDetail();
        detailParam.setActivityId(activityHeader.getId());
        List<Long> dbList = activityDetailMapper.pageList(detailParam)
                .stream().map(ActivityDetail::getId).collect(Collectors.toList());
        List<Long> reqIds = request.getItem().stream().map(ActivityDetail::getId).collect(Collectors.toList());
        List<Long> ids = dbList.stream().filter(s ->
                !reqIds.contains(s)).collect(Collectors.toList());
        for (Long id : ids) {
            activityDetailMapper.deleteById(id);
            activityContextDetailMapper.deleteByActivityDetailId(id);
        }
        activityContextDetailMapper.deleteByActivityId(activityHeader.getId());
        List<Object> listDesc = map.get("desc");
        if (!CollectionUtils.isEmpty(listDesc)) {
            int orderInt = 0;
            for (Object obj : listDesc) {
                ActivityContextDetail detail = JSON.parseObject(JSON.toJSONString(obj),ActivityContextDetail.class);
                detail.setActivityDetailId(0L);
                detail.setActivityId(activityHeader.getId());
                detail.setOrders(++orderInt);
                detail.setCreateBy("admin");
                detail.setUpdatedBy("admin");
                activityContextDetailMapper.insert(detail);
            }
        }
        for (ActivityDetail detail : request.getItem()) {
            if (detail.getId() != null) {
                activityContextDetailMapper.deleteByActivityDetailId(detail.getId());
            }
            detail.setUpdatedTime(null);
            detail.setUpdatedBy("admin");
            detail.setCreateBy("admin");
            detail.setActivityId(activityHeader.getId());
            detail.setOrderNum(order++);
            if (detail.getId() != null && dbList.contains(detail.getId())) {
                activityDetailMapper.update(detail);
            } else {
                detail.setId(null);
                activityDetailMapper.insert(detail);
            }

            List<ActivityContextDetail> list = detail.getContent();
            if (!CollectionUtils.isEmpty(list)) {
                int i = 0;
                for (ActivityContextDetail activityContextDetail : list) {
                    activityContextDetail.setActivityDetailId(detail.getId());
                    activityContextDetail.setOrders(i++);
                    activityContextDetail.setUpdatedBy("admin");
                    activityContextDetail.setCreateBy("admin");
                    activityContextDetail.setActivityId(0L);
                    activityContextDetailMapper.insert(activityContextDetail);
                }
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void joinActivity(JoinActivityRequest request) {
        RLock lock = null;
        try {
            String lockKey = RedissonLockService.getLockKey("joinActivity",request.getActivityId() + "");
            lock = RedissonLockService.getAndTryLock(lockKey);
            ActivityDetail paramDetail = new ActivityDetail();
            paramDetail.setActivityId(request.getActivityId());
            paramDetail.setUserId(VoteContext.session().getId());
            if (activityDetailMapper.pageList(paramDetail).size() > 0) {
                throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0013);
            }
            ActivityDetail detail = new ActivityDetail();
            BeanUtil.copyProperties(request,detail);
            int num = activityDetailMapper.getMaxNumByActiveId(request.getActivityId());
            detail.setOrderNum(num + 1);
            detail.setUserId(VoteContext.session().getId());
            detail.setUpdatedBy("admin");
            detail.setCreateBy("admin");
            detail.setStatus(0);
            detail.setId(null);
            activityDetailMapper.insert(detail);
            int i = 0;
            for (ActivityContextDetail form : request.getForms()) {
                form.setActivityId(0L);
                form.setActivityDetailId(detail.getId());
                form.setOrders(++i);
                form.setUpdatedBy("admin");
                form.setCreateBy("admin");
                activityContextDetailMapper.insert(form);
            }
        } finally {
            RedissonLockService.unlock(lock);
        }
    }

    @Override
    public ActivityDetailInfoResponse getActiveDetail(ActivityDetailRequest request) {
        if (request.getId() == null && "edit".equals(request.getType())) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0005);
        }
        if (request.getActivityDetailId() == null && "check".equals(request.getType())) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0005);
        }
        ActivityDetail detailParam = new ActivityDetail();
        if ("edit".equals(request.getType())) {
            detailParam.setActivityId(request.getId());
            detailParam.setUserId(VoteContext.session().getId());
        } else {
            detailParam.setId(request.getActivityDetailId());
        }
        List<ActivityDetail> list = activityDetailMapper.pageList(detailParam);
        if (CollectionUtils.isEmpty(list)) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0014);
        }
        ActivityDetail detail = list.get(0);
        ActivityDetailInfoResponse response = new ActivityDetailInfoResponse();
        BeanUtil.copyProperties(detail,response);
        ActivityContextDetail contextDetailParam = new ActivityContextDetail();
        contextDetailParam.setActivityDetailId(detail.getId());
        List<ActivityContextDetail> contextDetails = activityContextDetailMapper.pageList(contextDetailParam);
        response.setForms(contextDetails);
        return response;
    }

    @Override
    public void editActivity(JoinActivityRequest request) {
        RLock lock = null;
        try {
            String lockKey = RedissonLockService.getLockKey("joinActivity",request.getActivityId() + "");
            lock = RedissonLockService.getAndTryLock(lockKey);
            ActivityDetail paramDetail = new ActivityDetail();
            paramDetail.setActivityId(request.getActivityId());
            paramDetail.setUserId(VoteContext.session().getId());
            if (activityDetailMapper.pageList(paramDetail).size() <= 0 || request.getId() == null) {
                throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0014);
            }
            ActivityDetail detail = new ActivityDetail();
            BeanUtil.copyProperties(request,detail);
            // int num = activityDetailMapper.getMaxNumByActiveId(request.getActivityId());
            detail.setOrderNum(null);
            detail.setUserId(null);
            detail.setVoteNum(null);
            detail.setActivityId(null);
            detail.setStatus(0);
            activityDetailMapper.update(detail);
            activityContextDetailMapper.deleteByActivityDetailId(detail.getId());
            int i = 0;
            for (ActivityContextDetail form : request.getForms()) {
                form.setActivityId(0L);
                form.setActivityDetailId(detail.getId());
                form.setOrders(++i);
                form.setUpdatedBy("admin");
                form.setCreateBy("admin");
                activityContextDetailMapper.insert(form);
            }
        } finally {
            RedissonLockService.unlock(lock);
        }
    }

    @Override
    public Integer checkDetail(ActivityDetailCheckRequest request) {
        if (request.getId() == null || request.getStatus() == null) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0005);
        }
        ActivityDetail activityDetail = new ActivityDetail();
        activityDetail.setId(request.getId());
        activityDetail.setStatus(request.getStatus());
        activityDetail.setRejectReason(request.getRejectReason());
        activityDetailMapper.update(activityDetail);
        return 1;
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
        threadPoolTaskExecutor.submit(() -> {
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
        });
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
