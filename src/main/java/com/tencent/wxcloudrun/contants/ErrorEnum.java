package com.tencent.wxcloudrun.contants;

import lombok.Getter;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/3 16:47
 */
public enum ErrorEnum {

    VOTE_ERROR_0001(CommonConstant.ErrorCode.HEADER,"请先登录系统"),

    VOTE_ERROR_0002(CommonConstant.ErrorCode.DATA,"手机号不能为空"),

    VOTE_ERROR_0003(CommonConstant.ErrorCode.DATA,"用户不存在"),

    VOTE_ERROR_0004(CommonConstant.ErrorCode.DATA,"动态已被删除"),

    VOTE_ERROR_0005(CommonConstant.ErrorCode.DATA,"参数校验失败"),

    VOTE_ERROR_0006(CommonConstant.ErrorCode.DATA,"数据不存在或已被删除"),
    ;

    @Getter
    private int code;

    @Getter
    private String desc;

    ErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
