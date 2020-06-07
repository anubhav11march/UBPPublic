package com.ubptech.unitedbyplayers;

import android.net.Uri;

import java.util.HashMap;

/**
 * Created by Kylodroid on 06-06-2020.
 */
public interface ViewPageChange {
    void check(boolean done);

    void addPictureUri(HashMap<Integer, Uri> uris);

    void addPreferences(HashMap<String, Boolean> preferences);

    void addAge(int age);

    void addGender(int gender);

    void addAgeGroup(String ageGroup);

    void addDistance(int distance);
}
