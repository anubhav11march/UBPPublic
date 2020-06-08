package com.ubptech.unitedbyplayers;

/**
 * Created by Kylodroid on 08-06-2020.
 */
public interface CreateTeamChange {
    void setTeamName(String name);

    void setSport(String sport);

    void setOtherPlayers(boolean choice);

    void setAgeGroup(String ageGroup);

    void setMaxBet(int bet);

    void setDistance(int distance);

    void setBio(String bio);
}
