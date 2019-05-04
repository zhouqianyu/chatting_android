package com.bysj.chatting.bean;

import java.io.Serializable;

/**
 * Created by shaoxin on 19-5-2.
 * 用户（好友模型类）
 */

public class UserBean implements Serializable {

    private String uuid;
    private String username;
    private String describe;
    private String avatar;

    public UserBean() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", describe='" + describe + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
