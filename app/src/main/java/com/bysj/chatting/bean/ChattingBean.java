package com.bysj.chatting.bean;

/**
 * Created by shaoxin on 19-5-13.
 */

public class ChattingBean {
    private int chattingId; // 这条聊天的id
    private String senderId;  // 消息发送者id
    private String senderAvatar; // 消息发送者头像
    private String receiverId;    // 消息接受者id
    private String receiverAvatar;  // 消息接受者头像
    private String content;  // 消息内容

    private String myId;    // 我的id

    public int getChattingId() {
        return chattingId;
    }

    public void setChattingId(int chattingId) {
        this.chattingId = chattingId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverAvatar() {
        return receiverAvatar;
    }

    public void setReceiverAvatar(String receiverAvatar) {
        this.receiverAvatar = receiverAvatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    @Override
    public String toString() {
        return "ChattingBean{" +
                "chattingId=" + chattingId +
                ", senderId='" + senderId + '\'' +
                ", senderAvatar='" + senderAvatar + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", receiverAvatar='" + receiverAvatar + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
