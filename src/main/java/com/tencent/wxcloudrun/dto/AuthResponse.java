package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/10 17:44
 */
@Data
public class AuthResponse {

    private String token;

    private String name;

    private Long id;

    private boolean success;
}
