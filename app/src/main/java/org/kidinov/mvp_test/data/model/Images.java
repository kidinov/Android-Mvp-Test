package org.kidinov.mvp_test.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Images extends RealmObject {

    @SerializedName("low_resolution")
    @Expose
    private StandardResolution standardResolution;

    public StandardResolution getStandardResolution() {
        return standardResolution;
    }

    public void setStandardResolution(StandardResolution standardResolution) {
        this.standardResolution = standardResolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Images images = (Images) o;

        return getStandardResolution() != null ? getStandardResolution().equals(images.getStandardResolution()) : images.getStandardResolution() == null;

    }

    @Override
    public int hashCode() {
        return getStandardResolution() != null ? getStandardResolution().hashCode() : 0;
    }
}
