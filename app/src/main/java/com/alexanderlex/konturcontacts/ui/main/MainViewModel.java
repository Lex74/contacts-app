package com.alexanderlex.konturcontacts.ui.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.alexanderlex.konturcontacts.data.Repository;
import com.alexanderlex.konturcontacts.data.models.Person;
import com.alexanderlex.konturcontacts.utils.NameFilter;

import java.util.ArrayList;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private ArrayList<Person> originalPersonList;
    private MutableLiveData<ArrayList<Person>> personLiveData;
    private MutableLiveData<Throwable> exceptionLiveData;
    private Repository repository;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
    }


    public LiveData<ArrayList<Person>> personsData() {
        if (personLiveData == null) {
            personLiveData = new MutableLiveData<>();
            refreshData();
        }
        return personLiveData;
    }

    @SuppressLint("CheckResult")
    public void refreshData() {
        repository.getData().subscribe(data -> {
                    personLiveData.setValue(data);
                    originalPersonList = data;
                },
                throwable -> exceptionLiveData.setValue(throwable));
    }

    public LiveData<Throwable> exceptionData() {
        if (exceptionLiveData == null) {
            exceptionLiveData = new MutableLiveData<>();
        }
        return exceptionLiveData;
    }

    public void filterData(String value) {
        value = value.toLowerCase();
        NameFilter filter = new NameFilter(originalPersonList, filteredList -> {
            personLiveData.setValue(filteredList);
        });
        filter.filter(value);
    }
}


