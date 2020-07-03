package com.ubptech.unitedbyplayers;

import java.util.HashMap;

/**
 * Created by Kylodroid on 03-07-2020.
 */
class TeamCardDetails {
    private HashMap<String, String> photos;
    private String friendly, totalMatches, wonMatches, lostMatches, distance, name, sport, fullCode;

    TeamCardDetails(String friendly, String totalMatches,
                      String distance, HashMap<String, String> photos, String name, String sport, String fullCode){
        this.distance = distance;
        this.totalMatches = totalMatches;
        this.friendly = friendly;
        this.photos = photos;
        this.name = name;
        this.sport = sport;
        this.fullCode = fullCode;
    }

    public String getFullCode() {
        return fullCode;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
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

    public String getSport() {
        return sport;
    }

    public String getFriendly() {
        return friendly;
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

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setFriendly(String friendly) {
        this.friendly = friendly;
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
