package com.tencent.wxcloudrun.exception;


import com.tencent.wxcloudrun.contants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoteExceptionFactory {

    public static final String DEF_ERROR_CODE = "1301";

    private VoteExceptionFactory() {
    }

    public static VoteException getException(String error) {
        log.warn("System Processing Fail:{}", error);
        return new VoteException(DEF_ERROR_CODE, error);
    }

    public static VoteException getException(ErrorEnum errorEnum){
        return new VoteException(errorEnum.getCode(), errorEnum.getDesc());
    }

    public static VoteException getException(String code, String error) {
        log.warn("System Processing Fail:{}", error);
        return new VoteException(code, error);
    }

    public static VoteException getException(String error, Throwable throwable) {
        log.warn("System Processing Fail:{}", error);
        return new VoteException(DEF_ERROR_CODE, error, throwable);
    }

    public static VoteException getException(String code, String error, Throwable throwable) {
        log.warn("System Processing Fail:{}", error);
        return new VoteException(code, error, throwable);
    }
}
