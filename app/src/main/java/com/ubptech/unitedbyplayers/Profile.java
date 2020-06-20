package com.ubptech.unitedbyplayers;

/**
 * Created by Kylodroid on 20-06-2020.
 */
class Profile {
    String teamName, sport, fullCode;

    Profile(String teamName, String sport, String fullCode){
        this.teamName = teamName;
        this.sport = sport;
        this.fullCode = fullCode;
    }

    public String getFullCode() {
        return fullCode;
    }

    public String getSport() {
        return sport;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
