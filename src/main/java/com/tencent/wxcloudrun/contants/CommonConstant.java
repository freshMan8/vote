package com.tencent.wxcloudrun.contants;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/3 15:31
 */
public interface CommonConstant {

    String AES128_PUBLIC_KEY = "8679186867918600";

    String TOKEN = "token";

    String SPLIT_SEQ = ";";

    String DATE_FOMATE = "yyyy-MM-dd HH:mm:ss";

    String USER_INFO = "USER:INFO:KEY";

    String WX_GET_PHONE_NUM_URL = "http://api.weixin.qq.com/wxa/business/getuserphonenumber";

    interface UserType {
        int ROBOT = 0;

        int REAL = 1;
    }

    interface ErrorCode {

        //服务器错误
        int SVR = 500;

        //逻辑错误
        int LOGIC = 1600;

        // 数据校验错误
        int DATA = 1301;

        // header 校验错误
        int HEADER = 1302;

        //管理员错误
        int ADMIN_ERROR = 2401;
    }
}
