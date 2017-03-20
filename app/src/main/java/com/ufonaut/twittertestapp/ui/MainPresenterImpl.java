package com.ufonaut.twittertestapp.ui;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.ufonaut.twittertestapp.Constants;
import com.ufonaut.twittertestapp.R;
import com.ufonaut.twittertestapp.TwitterApp;
import com.ufonaut.twittertestapp.api.ApiService;
import com.ufonaut.twittertestapp.api.Headers;
import com.ufonaut.twittertestapp.api.model.AccessToken;
import com.ufonaut.twittertestapp.api.model.ErrorResponse;
import com.ufonaut.twittertestapp.api.model.Tweet;
import com.ufonaut.twittertestapp.di.ApiModule;
import com.ufonaut.twittertestapp.di.DataModule;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.HttpException;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class MainPresenterImpl implements MainPresenter {

    private MainCallback mainCallback;
    private Subscription subscription;
    private ArrayList<Tweet> receivedTweets;
    private String screenName;

    @Inject
    ApiService apiService;

    @Inject
    Scheduler apiScheduler;

    @Inject
    @ApiModule.HeadersQualifier
    Headers headers;

    @Inject
    @DataModule.AppContext
    Context context;

    MainPresenterImpl(MainCallback mainCallback) {
        TwitterApp.getComponent().inject(this);
        this.mainCallback = mainCallback;
    }

    private void login() {
        if (mainCallback != null) {
            mainCallback.showProgress();
        }

        if(subscription != null)
            subscription.unsubscribe();

        subscription = apiService.login(Constants.CLIENT_CREDENTIALS_AUTH_PARAM)
                .flatMap(new Func1<AccessToken, Observable<? extends ArrayList<Tweet>>>() {
                    @Override
                    public Observable<? extends ArrayList<Tweet>> call(AccessToken accessToken) {
                        saveAccessToken(accessToken);
                        return apiService.getUsersTimeLine(screenName);
                    }
                })
                .subscribeOn(apiScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Tweet>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetTimeLineError(e);
                    }

                    @Override
                    public void onNext(ArrayList<Tweet> tweets) {
                        onGetTimeLineSuccess(tweets);
                    }
                });
    }

    private void saveAccessToken(AccessToken accessToken) {
        headers.setAccessToken(accessToken.getAccessToken());
    }

    @Override
    public void getUserTimeLine(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            retainUserData(savedInstanceState);
        } else {
            if (screenName != null) {
                if (!headers.hasAccessToken()) {
                    login();
                } else {
                    fetchUserTimeline();
                }
            } else {
                mainCallback.hideProgress();
                mainCallback.onUserTimelineError(context.getString(R.string.enter_some_username));
            }
        }
    }

    private void fetchUserTimeline() {
        if (mainCallback != null) {
            mainCallback.showProgress();
        }

        if(subscription != null)
            subscription.unsubscribe();

        subscription = apiService.getUsersTimeLine(screenName)
                .subscribeOn(apiScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Tweet>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetTimeLineError(e);
                    }

                    @Override
                    public void onNext(ArrayList<Tweet> tweets) {
                        onGetTimeLineSuccess(tweets);
                    }
                });
    }

    private void onGetTimeLineSuccess(ArrayList<Tweet> tweets) {
        receivedTweets = tweets;
        mainCallback.hideProgress();
        if (tweets != null)
            mainCallback.onUserTimelineSuccess(tweets);
    }

    private void onGetTimeLineError(Throwable e) {
        mainCallback.hideProgress();
        mainCallback.onUserTimelineError(getError(e));
    }

    @Override
    public void retainUserData(Bundle savedInstance) {
        receivedTweets = savedInstance.getParcelableArrayList(Constants.KEY_SAVED_TWEETS);
        screenName = savedInstance.getString(Constants.KEY_SCREEN_NAME);
        if (receivedTweets != null)
            mainCallback.onUserTimelineSuccess(receivedTweets);
    }

    @Override
    public void onPause() {
        cancelGetTimelineRequest();
    }

    private void cancelGetTimelineRequest() {
        if(subscription != null)
            subscription.unsubscribe();
    }

    @Override
    public void onSavedInstanceState(Bundle savedInstance) {
        savedInstance.putParcelableArrayList(Constants.KEY_SAVED_TWEETS, receivedTweets);
        savedInstance.putString(Constants.KEY_SCREEN_NAME, screenName);
    }

    @Override
    public void saveScreenName(String screenName) {
        if (screenName != null && !screenName.contains(Constants.BLANK)) {
            this.screenName = screenName;
            getUserTimeLine(null);
        } else {
            mainCallback.onUserTimelineError(context.getString(R.string.spaces));
        }
    }

    @Override
    public String getScreenName() {
        return screenName == null ? Constants.EMPTY_STRING : screenName;
    }

    private String getError(Throwable e) {
        String message = e.getMessage();
        try {
            String errorBody =  ((HttpException) e).response().errorBody().string();
            ErrorResponse response = new Gson().fromJson(errorBody, ErrorResponse.class);
            if (response != null && response.getErrors() != null && response.getErrors().size() > 0)
                message = response.getErrors().get(0).getMessage();
        } catch (Exception error) {
            error.printStackTrace();
        }
        return message;
    }
}
