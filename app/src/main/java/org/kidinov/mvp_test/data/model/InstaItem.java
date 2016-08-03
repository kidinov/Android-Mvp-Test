package org.kidinov.mvp_test.data.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class InstaItem extends RealmObject implements Comparable<InstaItem>{
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("images")
    @Expose
    private Images images;
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("created_time")
    @Expose
    private Long createdTime;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstaItem item = (InstaItem) o;

        return getId() != null ? getId().equals(item.getId()) : item.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "InstaItem{" +
                "location=" + location +
                ", images=" + images +
                ", id='" + id + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }


    @Override
    public int compareTo(@NonNull InstaItem instaItem) {
        return getCreatedTime().compareTo(instaItem.getCreatedTime());
    }
}
