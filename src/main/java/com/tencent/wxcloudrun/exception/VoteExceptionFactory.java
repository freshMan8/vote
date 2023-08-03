package com.tencent.wxcloudrun.exception;


import com.tencent.wxcloudrun.contants.CommonConstant;
import com.tencent.wxcloudrun.contants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoteExceptionFactory {

    private VoteExceptionFactory() {
    }

    public static VoteException getException(String error) {
        log.warn("System Processing Fail:{}", error);
        return new VoteException(CommonConstant.ErrorCode.DATA, error);
    }

    public static VoteException getException(ErrorEnum errorEnum){
        return new VoteException(errorEnum.getCode(), errorEnum.getDesc());
    }

    public static VoteException getException(int code, String error) {
        log.warn("System Processing Fail:{}", error);
        return new VoteException(code, error);
    }

    public static VoteException getException(String error, Throwable throwable) {
        log.warn("System Processing Fail:{}", error);
        return new VoteException(CommonConstant.ErrorCode.SVR, error, throwable);
    }

    public static VoteException getException(int code, String error, Throwable throwable) {
        log.warn("System Processing Fail:{}", error);
        return new VoteException(code, error, throwable);
    }
}
