package com.ufonaut.twittertestapp;

import android.app.Application;

import com.ufonaut.twittertestapp.di.ApiModule;
import com.ufonaut.twittertestapp.di.AppComponent;
import com.ufonaut.twittertestapp.di.DaggerAppComponent;
import com.ufonaut.twittertestapp.di.DataModule;


public class TwitterApp extends Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = buildComponent();
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .apiModule(new ApiModule())
                .dataModule(new DataModule(this))
                .build();
    }

    public static AppComponent getComponent() {
        return appComponent;
    }
}
