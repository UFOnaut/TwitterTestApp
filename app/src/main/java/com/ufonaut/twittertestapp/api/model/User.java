package com.ufonaut.twittertestapp.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    @SerializedName("screen_name")
    private String screenName;
    private String name;
    private long id;
    private List<Integer> indices;

    public String getScreenName() {
        return screenName;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public List<Integer> getIndices() {
        return indices;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.screenName);
        dest.writeString(this.name);
        dest.writeLong(this.id);
        dest.writeList(this.indices);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.screenName = in.readString();
        this.name = in.readString();
        this.id = in.readLong();
        this.indices = new ArrayList<>();
        in.readList(this.indices, Integer.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
