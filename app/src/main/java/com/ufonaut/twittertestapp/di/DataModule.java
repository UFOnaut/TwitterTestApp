package com.ufonaut.twittertestapp.di;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.ufonaut.twittertestapp.api.ApiService;
import com.ufonaut.twittertestapp.api.Headers;

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
public class DataModule {

    private Context context;

    public DataModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @AppContext
    Context provideAppContext() {
        return context;
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AppContext {
    }
}
