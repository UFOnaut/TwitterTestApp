package com.ufonaut.twittertestapp.ui;

import android.os.Bundle;

interface MainPresenter {
    void getUserTimeLine(Bundle savedInstance);
    void retainUserData(Bundle savedInstance);
    void onPause();
    void onSavedInstanceState(Bundle savedInstance);
    void saveScreenName(String screenName);
    String getScreenName();
}
