package com.alexanderlex.konturcontacts.di;

import com.alexanderlex.konturcontacts.ui.main.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                RepoModule.class,
                MainViewModelModule.class
        }
)
public interface AppComponent {
    void inject(MainFragment mainFragment);
}
