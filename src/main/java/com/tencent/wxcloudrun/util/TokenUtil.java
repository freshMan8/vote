package com.tencent.wxcloudrun.util;

import com.tencent.wxcloudrun.contants.CommonConstant;
import com.tencent.wxcloudrun.contants.ErrorEnum;
import com.tencent.wxcloudrun.exception.VoteExceptionFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/3 16:27
 */
public enum TokenUtil {

    INSTANCE;

    public String getPhoneNum(String token) {
        if (StringUtils.isEmpty(token)) {
            throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0001);
        }
        String phoneVal = EncryptUtil.Decrypt(token);
        if (StringUtils.isNotEmpty(phoneVal)) {
            return phoneVal.split(CommonConstant.SPLIT_SEQ)[0];
        }
        throw VoteExceptionFactory.getException(ErrorEnum.VOTE_ERROR_0001);
    }

    public String generateToken(String phoneNum) {
        return EncryptUtil.Encrypt(buildToken(phoneNum));
    }

    public String buildToken(String phoneNum) {
        String date = DateFormatUtils.format(new Date(),CommonConstant.DATE_FOMATE);
        return phoneNum + CommonConstant.SPLIT_SEQ + date;
    }
}
