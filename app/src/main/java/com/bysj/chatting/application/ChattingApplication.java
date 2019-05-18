package com.bysj.chatting.application;

import android.app.Application;

import com.bysj.chatting.service.MqttService;

import java.io.Serializable;

/**
 * Created by shaoxin on 19-5-12.
 */

public class ChattingApplication extends Application implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private String miId;
    private String myAvatar;
    private MqttService mqttService;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMiId() {
        return miId;
    }

    public void setMiId(String miId) {
        this.miId = miId;
    }

    public String getMyAvatar() {
        return myAvatar;
    }

    public void setMyAvatar(String myAvatar) {
        this.myAvatar = myAvatar;
    }

    public MqttService getMqttService() {
        return mqttService;
    }

    public void setMqttService(MqttService mqttService) {
        this.mqttService = mqttService;
    }
}
