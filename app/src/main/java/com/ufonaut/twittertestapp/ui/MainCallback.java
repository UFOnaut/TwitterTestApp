package com.ufonaut.twittertestapp.ui;

import com.ufonaut.twittertestapp.api.model.Tweet;

import java.util.List;

interface MainCallback {
    void showProgress();
    void hideProgress();
    void onUserTimelineSuccess(List<Tweet> tweets);
    void onUserTimelineError(String error);
    void onLoginError(String error);
}
