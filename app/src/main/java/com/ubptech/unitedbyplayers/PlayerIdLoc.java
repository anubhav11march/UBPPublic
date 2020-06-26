package com.ubptech.unitedbyplayers;

/**
 * Created by Kylodroid on 24-06-2020.
 */
class PlayerIdLoc {
    String uid;
    double lat, lon;
    PlayerIdLoc(String uid, double lat, double lon){
        this.lat = lat;
        this.lon = lon;
        this.uid = uid;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getUid() {
        return uid;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
