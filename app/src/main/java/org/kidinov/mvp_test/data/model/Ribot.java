package org.kidinov.mvp_test.data.model;

import android.support.annotation.NonNull;

import io.realm.RealmObject;

public class Ribot extends RealmObject implements Comparable<Ribot> {
    private Profile profile;

    public Ribot() {
    }

    public Ribot(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public int compareTo(@NonNull Ribot another) {
        return profile.getName().getFirst()
                .compareToIgnoreCase(another.profile.getName().getFirst());
    }
}

