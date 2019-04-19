package com.alexanderlex.konturcontacts.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.alexanderlex.konturcontacts.data.models.Person;

@Database(entities = {Person.class}, version = 1)
public abstract class ContactsDatabase extends RoomDatabase {
    public abstract ContactsDao getContactsDao();
}
