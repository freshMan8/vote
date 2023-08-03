package com.tencent.wxcloudrun.util;

import com.tencent.wxcloudrun.model.AuthSession;

public class VoteContext {

    private static final ThreadLocal<AuthSession> LOCAL = new ThreadLocal<>();

    public static void setSession(AuthSession session) {
        LOCAL.set(session);
    }

    public static AuthSession session() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
