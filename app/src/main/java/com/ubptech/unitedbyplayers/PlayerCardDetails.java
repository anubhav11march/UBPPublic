package com.ubptech.unitedbyplayers;

import java.util.HashMap;

/**
 * Created by Kylodroid on 24-06-2020.
 */
class PlayerCardDetails {
    private HashMap<String, String> photos;
    private String gender, totalMatches, wonMatches, lostMatches, distance, name, uid;

    PlayerCardDetails(String gender, String totalMatches,
                      String distance, HashMap<String, String> photos, String name, String uid){
        this.distance = distance;
        this.totalMatches = totalMatches;
        this.gender = gender;
        this.photos = photos;
        this.name = name;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public HashMap<String, String> getPhotos() {
        return photos;
    }

    public String getGender() {
        return gender;
    }

    public String getTotalMatches() {
        return totalMatches;
    }

    public String getLostMatches() {
        return lostMatches;
    }

    public String getWonMatches() {
        return wonMatches;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLostMatches(String lostMatches) {
        this.lostMatches = lostMatches;
    }

    public void setPhotos(HashMap<String, String> photos) {
        this.photos = photos;
    }

    public void setWonMatches(String wonMatches) {
        this.wonMatches = wonMatches;
    }

    public void setTotalMatches(String totalMatches) {
        this.totalMatches = totalMatches;
    }
}
