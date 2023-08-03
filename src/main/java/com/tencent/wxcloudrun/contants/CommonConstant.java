package com.tencent.wxcloudrun.contants;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/3 15:31
 */
public interface CommonConstant {

    String AES128_PUBLIC_KEY = "86791868679186";

    String TOKEN = "token";

    String SPLIT_SEQ = ";";

    String DATE_FOMATE = "yyyy-MM-dd HH:mm:ss";

    String USER_INFO = "USER:INFO:KEY";

    interface ErrorCode {
        //服务器错误
        String SVR = "500";

        //逻辑错误
        String LOGIC = "1600";

        // 数据校验错误
        String DATA = "1301";

        // header 校验错误
        String HEADER = "1302";

        //管理员错误
        String ADMIN_ERROR = "2401";
    }
}
