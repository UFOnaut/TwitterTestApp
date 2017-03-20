package com.ufonaut.twittertestapp.api.model;

import com.google.gson.annotations.SerializedName;

public class Credentials {
    @SerializedName("screen_name")
    private String screenName;

    public String getScreenName() {
        return screenName;
    }
}
