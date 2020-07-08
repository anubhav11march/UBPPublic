package com.ubptech.unitedbyplayers;

/**
 * Created by Kylodroid on 08-07-2020.
 */
class Message {
    private String senderUid, receiverUid, messageType;
    private Object messageData;
    private long timestamp;

    Message(){}

    Message(String senderUid, String receiverUid, String messageType, Object messageData, long timestamp){
        this.messageData = messageData;
        this.messageType = messageType;
        this.receiverUid = receiverUid;
        this.senderUid = senderUid;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object getMessageData() {
        return messageData;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessageData(Object messageData) {
        this.messageData = messageData;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }
}
