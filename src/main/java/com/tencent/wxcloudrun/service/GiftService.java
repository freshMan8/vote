package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.GiftRequest;
import com.tencent.wxcloudrun.model.Gift;

import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/14 15:45
 */
public interface GiftService {

    List<Gift> pageList(GiftRequest request);
}
