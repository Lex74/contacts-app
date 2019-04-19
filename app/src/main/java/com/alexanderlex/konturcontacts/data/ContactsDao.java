package com.alexanderlex.konturcontacts.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.util.Log;

import com.alexanderlex.konturcontacts.data.models.Person;

import java.util.List;

@Dao
public abstract class ContactsDao {
    @Query("SELECT * FROM person")
    public abstract List<Person> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Person person);

    @Transaction
    public void batchInsert(List<Person> persons) {
        Log.d(ContactsDao.class.getName(), "batchInsert: " + persons.size());
        for (Person person :
                persons) {
            insert(person);
        }
    }

    @Update
    public abstract void update(Person person);

    @Delete
    public abstract void delete(Person person);
}
