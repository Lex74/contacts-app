package com.alexanderlex.konturcontacts.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.alexanderlex.konturcontacts.data.models.Person;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Person> personLiveData = new MutableLiveData<>();

    public void select(Person person) {
        personLiveData.setValue(person);
    }

    public LiveData<Person> getSelected() {
        return personLiveData;
    }
}
