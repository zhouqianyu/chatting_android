package com.bysj.chatting.application;

import android.app.Application;

import java.io.Serializable;

/**
 * Created by shaoxin on 19-5-12.
 */

public class ChattingApplication extends Application implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
