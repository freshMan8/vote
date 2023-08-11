package com.tencent.wxcloudrun.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.tencent.wxcloudrun.contants.CommonConstant;
import com.tencent.wxcloudrun.contants.ErrorEnum;
import com.tencent.wxcloudrun.dto.WxPhoneNumResult;
import com.tencent.wxcloudrun.exception.VoteExceptionFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Map;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/3 16:27
 */
public enum TokenUtil {

    INSTANCE;

    public String getPhoneNumByWX(String code) {
        Map<String,String> paramMap = Maps.newHashMap();
        paramMap.put("code",code);
        String result = HttpUtil.doPost(CommonConstant.WX_GET_PHONE_NUM_URL, JSON.toJSONString(paramMap),Maps.newHashMap());
        WxPhoneNumResult rs = JSON.parseObject(result, WxPhoneNumResult.class);
        if (rs != null) {
            if (rs.getErrcode() == 0) {
                String phoneNum = rs.getPhone_info().getPurePhoneNumber();
                return generateToken(phoneNum);
            } else {
                throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0007);
            }
        }
        return null;
    }

    public String getPhoneNumByAuthToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0001);
        }
        String phoneVal = EncryptUtil.Decrypt(token);
        if (StringUtils.isNotEmpty(phoneVal)) {
            return phoneVal.split(CommonConstant.SPLIT_SEQ)[0];
        }
        throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0001);
    }

    public static String generateToken(String phoneNum) {
        return EncryptUtil.Encrypt(buildToken(phoneNum));
    }

    public static void main(String[] args) {
        System.out.println(generateToken("18727582327"));
    }

    public static String buildToken(String phoneNum) {
        String date = DateFormatUtils.format(new Date(),CommonConstant.DATE_FOMATE);
        return phoneNum + CommonConstant.SPLIT_SEQ + date;
    }
}
