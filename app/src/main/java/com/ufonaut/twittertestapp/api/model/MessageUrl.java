package com.ufonaut.twittertestapp.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MessageUrl implements Parcelable {
    private String url;
    private List<Integer> indices;

    public String getUrl() {
        return url;
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
        dest.writeString(this.url);
        dest.writeList(this.indices);
    }

    public MessageUrl() {
    }

    protected MessageUrl(Parcel in) {
        this.url = in.readString();
        this.indices = new ArrayList<Integer>();
        in.readList(this.indices, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<MessageUrl> CREATOR = new Parcelable.Creator<MessageUrl>() {
        @Override
        public MessageUrl createFromParcel(Parcel source) {
            return new MessageUrl(source);
        }

        @Override
        public MessageUrl[] newArray(int size) {
            return new MessageUrl[size];
        }
    };
}
