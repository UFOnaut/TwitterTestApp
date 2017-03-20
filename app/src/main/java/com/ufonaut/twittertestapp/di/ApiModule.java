package com.ufonaut.twittertestapp.di;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.ufonaut.twittertestapp.api.Headers;
import com.ufonaut.twittertestapp.api.ApiService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Module
public class ApiModule {

    private static final String PRODUCTION_API_URL = "https://api.twitter.com/";

    @Provides
    @Singleton
    ApiService provideApiService(@RetrofitQualifier Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    Scheduler provideApiScheduler() {
        return Schedulers.newThread();
    }

    @Provides
    @RetrofitQualifier
    @Singleton
    Retrofit provideRestAdapter(@HeadersQualifier Headers headers) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.interceptors().add(loggingInterceptor);
        okHttpClient.interceptors().add(headers);

        return new Retrofit.Builder()
                .baseUrl(PRODUCTION_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @HeadersQualifier
    Headers provideAccessToken() {
        return new Headers();
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface RetrofitQualifier {
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface HeadersQualifier {
    }
}
