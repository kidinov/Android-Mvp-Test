package org.kidinov.mvp_test.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class InstaFeed extends RealmObject {
    @SerializedName("items")
    @Expose
    private RealmList<InstaItem> instaItems = new RealmList<>();
    @SerializedName("more_available")
    @Expose
    private Boolean moreAvailable;

    public List<InstaItem> getInstaItems() {
        return instaItems;
    }

    public void setInstaItems(RealmList<InstaItem> instaItems) {
        this.instaItems = instaItems;
    }

    public Boolean getMoreAvailable() {
        return moreAvailable;
    }

    public void setMoreAvailable(Boolean moreAvailable) {
        this.moreAvailable = moreAvailable;
    }

}
