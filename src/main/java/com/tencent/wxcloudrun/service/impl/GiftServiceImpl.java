package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.GiftMapper;
import com.tencent.wxcloudrun.dto.GiftRequest;
import com.tencent.wxcloudrun.model.Gift;
import com.tencent.wxcloudrun.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/14 15:45
 */
@Service
public class GiftServiceImpl implements GiftService {

    @Autowired
    private GiftMapper giftMapper;

    @Override
    public List<Gift> pageList(GiftRequest request) {
        Gift gift = new Gift();
        gift.setActivityType(request.getType());
        gift.setEnable(1);
        List<Gift> list = giftMapper.pageList(gift);
        list.sort(Comparator.comparing(Gift::getVoteNum,Comparator.reverseOrder()));
        return list;
    }
}
