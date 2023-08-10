package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/10 17:36
 */
@Data
public class WxPhoneNumResult {

    private Integer errcode;

    private String errormsg;

    private PhoneInfo phone_info;

    @Data
    public static class PhoneInfo {

        private String phoneNumber;

        private String purePhoneNumber;

        private String countryCode;
    }
}
