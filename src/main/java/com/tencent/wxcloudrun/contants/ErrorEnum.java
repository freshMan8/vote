package com.tencent.wxcloudrun.contants;

import lombok.Getter;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/3 16:47
 */
public enum ErrorEnum {

    VOTE_ERROR_0001(CommonConstant.ErrorCode.HEADER,"请先登录系统");

    @Getter
    private String code;

    @Getter
    private String desc;

    ErrorEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
