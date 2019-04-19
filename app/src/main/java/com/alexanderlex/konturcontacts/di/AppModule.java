package com.alexanderlex.konturcontacts.di;

import android.content.Context;

import com.alexanderlex.konturcontacts.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module()
public class AppModule {
    App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app;
    }
}
