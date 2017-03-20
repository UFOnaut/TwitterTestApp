package com.ufonaut.twittertestapp.di;


import com.ufonaut.twittertestapp.ui.MainPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApiModule.class, DataModule.class})
public interface AppComponent {
    void inject(MainPresenterImpl mainPresenter);
}
