package uk.co.ribot.androidboilerplate.data.model;

import android.support.annotation.Nullable;

import java.util.Date;

import io.realm.RealmObject;

public class Profile extends RealmObject {
    private Name name;
    private String email;
    private String hexColor;
    private Date dateOfBirth;
    @Nullable
    private String bio;
    @Nullable
    private String avatar;

    public Profile() {
    }

    private Profile(Builder builder) {
        setName(builder.name);
        setEmail(builder.email);
        setHexColor(builder.hexColor);
        setDateOfBirth(builder.dateOfBirth);
        setBio(builder.bio);
        setAvatar(builder.avatar);
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Nullable
    public String getBio() {
        return bio;
    }

    public void setBio(@Nullable String bio) {
        this.bio = bio;
    }

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
    }


    public static final class Builder {
        private Name name;
        private String email;
        private String hexColor;
        private Date dateOfBirth;
        private String bio;
        private String avatar;

        public Builder() {
        }

        public Builder setName(Name val) {
            name = val;
            return this;
        }

        public Builder setEmail(String val) {
            email = val;
            return this;
        }

        public Builder setHexColor(String val) {
            hexColor = val;
            return this;
        }

        public Builder setDateOfBirth(Date val) {
            dateOfBirth = val;
            return this;
        }

        public Builder setBio(String val) {
            bio = val;
            return this;
        }

        public Builder setAvatar(String val) {
            avatar = val;
            return this;
        }

        public Profile build() {
            return new Profile(this);
        }
    }
}
