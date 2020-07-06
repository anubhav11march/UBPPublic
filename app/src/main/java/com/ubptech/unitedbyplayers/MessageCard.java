package com.ubptech.unitedbyplayers;

/**
 * Created by Kylodroid on 06-07-2020.
 */
class MessageCard {
    private String lastMessage, name, photo, uid, sport;
    private long timestamp;
    private boolean newMessage;

    public MessageCard(){}

    public MessageCard(long timestamp, String lastMessage, String name, String photo,
                       String uid, String sport, boolean newMessage) {
        this.lastMessage = lastMessage;
        this.name = name;
        this.newMessage = newMessage;
        this.photo = photo;
        this.sport = sport;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getSport() {
        return sport;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setNewMessage(boolean newMessage) {
        this.newMessage = newMessage;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isNewMessage() {
        return newMessage;
    }
}

