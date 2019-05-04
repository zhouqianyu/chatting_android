package com.bysj.chatting.bean;

import java.io.Serializable;

/**
 * Created by shaoxin on 19-5-2.
 * 聊天的消息模型类
 */

public class MessageBean implements Serializable{
    private int id;
    private String friendName;  // 好友昵称
    private String friendAvatar;    // 好友头像
    private String friendId;   // 好友id
    private String content;     // 消息内容
    private String time; // 消息发送时间
    private int isDelivery; // 是否已读

    public MessageBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendAvatar() {
        return friendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {
        this.friendAvatar = friendAvatar;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(int isDelivery) {
        this.isDelivery = isDelivery;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "id=" + id +
                ", friendName='" + friendName + '\'' +
                ", friendAvatar='" + friendAvatar + '\'' +
                ", friendId='" + friendId + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", isDelivery=" + isDelivery +
                '}';
    }
}
