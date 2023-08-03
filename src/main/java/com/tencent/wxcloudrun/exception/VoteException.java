package com.tencent.wxcloudrun.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 异常
 *
 * @author 3832
 * @date 2023/8/3 16:38
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class VoteException extends RuntimeException {

    private String errCode;

    private String errMsg;

    private Throwable causeThrowable;


    public VoteException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public VoteException(String errCode, Throwable throwable) {
        super(throwable);
        this.errCode = errCode;
        setCauseThrowable(throwable);
    }

    public VoteException(String errCode, String errMsg, Throwable throwable) {
        super(errMsg, throwable);
        this.errCode = errCode;
        this.errMsg = errMsg;
        setCauseThrowable(throwable);
    }

    public String getErrMsg() {
        if (StringUtils.isNotBlank(this.errMsg)) {
            return errMsg;
        }
        if (this.causeThrowable != null) {
            return causeThrowable.getMessage();
        }
        return "";
    }

    public void setCauseThrowable(Throwable throwable) {
        this.causeThrowable = getCauseThrowable(throwable);
    }

    private Throwable getCauseThrowable(Throwable t) {
        if (t.getCause() == null) {
            return t;
        }
        return getCauseThrowable(t.getCause());
    }
}
