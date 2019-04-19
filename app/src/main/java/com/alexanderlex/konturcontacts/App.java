package com.alexanderlex.konturcontacts;

import android.app.Application;

import com.alexanderlex.konturcontacts.di.AppComponent;
import com.alexanderlex.konturcontacts.di.AppModule;
import com.alexanderlex.konturcontacts.di.DaggerAppComponent;

public class App extends Application {
    AppComponent appComponent;

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }

        return appComponent;
    }
}
