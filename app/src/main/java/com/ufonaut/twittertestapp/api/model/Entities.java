package com.ufonaut.twittertestapp.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Entities implements Parcelable {
    @SerializedName("hashtags")
    private List<HashTag> hashTags;
    @SerializedName("user_mentions")
    private List<User> userMentions;
    private List<MessageUrl> urls;

    public List<HashTag> getHashTags() {
        return hashTags;
    }

    public List<User> getUserMentions() {
        return userMentions;
    }

    public List<MessageUrl> getUrls() {
        return urls;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.hashTags);
        dest.writeTypedList(this.userMentions);
        dest.writeList(this.urls);
    }

    public Entities() {
    }

    protected Entities(Parcel in) {
        this.hashTags = new ArrayList<>();
        in.readList(this.hashTags, HashTag.class.getClassLoader());
        this.userMentions = in.createTypedArrayList(User.CREATOR);
        this.urls = new ArrayList<>();
        in.readList(this.urls, MessageUrl.class.getClassLoader());
    }

    public static final Creator<Entities> CREATOR = new Creator<Entities>() {
        @Override
        public Entities createFromParcel(Parcel source) {
            return new Entities(source);
        }

        @Override
        public Entities[] newArray(int size) {
            return new Entities[size];
        }
    };
}
