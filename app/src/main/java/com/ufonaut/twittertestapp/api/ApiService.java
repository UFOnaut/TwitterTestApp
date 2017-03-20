package com.ufonaut.twittertestapp.api;

import com.squareup.okhttp.Response;
import com.ufonaut.twittertestapp.api.model.AccessToken;
import com.ufonaut.twittertestapp.api.model.Credentials;
import com.ufonaut.twittertestapp.api.model.Tweet;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

import static com.ufonaut.twittertestapp.Constants.LOGIN_URL;
import static com.ufonaut.twittertestapp.Constants.USER_TIMELINE_URL;

public interface ApiService {

    @FormUrlEncoded
    @POST(LOGIN_URL)
    Observable<AccessToken> login(@Field("grant_type") String grantType);

    @GET(USER_TIMELINE_URL)
    Observable<ArrayList<Tweet>> getUsersTimeLine(@Query("screen_name") String name);
}
