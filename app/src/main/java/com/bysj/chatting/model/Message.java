package com.bysj.chatting.model;

/**
 * Created by shaoxin on 19-5-2.
 * 聊天的消息模型类
 */

public class Message {
    private int id;
    private String uuid_from;
    private String uuid_to;
    private String message;
    private String created_at;
    private int is_delivery;

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid_from() {
        return uuid_from;
    }

    public void setUuid_from(String uuid_from) {
        this.uuid_from = uuid_from;
    }

    public String getUuid_to() {
        return uuid_to;
    }

    public void setUuid_to(String uuid_to) {
        this.uuid_to = uuid_to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getIs_delivery() {
        return is_delivery;
    }

    public void setIs_delivery(int is_delivery) {
        this.is_delivery = is_delivery;
    }
}
