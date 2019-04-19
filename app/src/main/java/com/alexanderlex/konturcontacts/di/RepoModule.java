package com.alexanderlex.konturcontacts.di;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.alexanderlex.konturcontacts.data.ContactsDao;
import com.alexanderlex.konturcontacts.data.ContactsDatabase;
import com.alexanderlex.konturcontacts.data.RemoteRepo;
import com.alexanderlex.konturcontacts.data.Repository;
import com.alexanderlex.konturcontacts.data.SharedPrefs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepoModule {

    @Provides
    @Singleton
    Repository provideRepo(ContactsDao db, RemoteRepo remoteRepo, SharedPrefs sharedPrefs) {
        return new Repository(db, remoteRepo, sharedPrefs);
    }

    @Provides
    @Singleton
    RemoteRepo provideRemoteRepo() {
        return new RemoteRepo();
    }

    @Provides
    @Singleton
    ContactsDatabase provideDatabase(@NonNull Context context) {
        return Room.databaseBuilder(context, ContactsDatabase.class, "contacts.db").build();
    }

    @Provides
    @Singleton
    ContactsDao provideContactsDao(@NonNull ContactsDatabase db) {
        return db.getContactsDao();
    }

    @Provides
    @Singleton
    SharedPrefs provideSharedPreferences(@NonNull Context context) {
        return new SharedPrefs(context);
    }
}
