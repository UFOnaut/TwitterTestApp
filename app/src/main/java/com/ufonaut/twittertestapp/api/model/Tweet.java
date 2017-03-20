package com.ufonaut.twittertestapp.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Tweet implements Parcelable {
    @SerializedName("created_at")
    private String createdAt;
    private String text;
    private User user;
    private Entities entities;
    @SerializedName("retweeted_status")
    private Tweet retweetedStatus;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public Entities getEntities() {
        return entities;
    }

    public User getUser() {
        return user;
    }

    public Tweet getRetweetedStatus() {
        return retweetedStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createdAt);
        dest.writeString(this.text);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.entities, flags);
        dest.writeParcelable(this.retweetedStatus, flags);
    }

    public Tweet() {
    }

    protected Tweet(Parcel in) {
        this.createdAt = in.readString();
        this.text = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.entities = in.readParcelable(Entities.class.getClassLoader());
        this.retweetedStatus = in.readParcelable(Tweet.class.getClassLoader());
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
