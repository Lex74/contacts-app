package com.alexanderlex.konturcontacts.data;

import com.alexanderlex.konturcontacts.data.models.Person;
import com.alexanderlex.konturcontacts.utils.DateTimeUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Repository {
    private static final int DIFF_TO_REFRESH_IN_SECONDS = 60;
    private ContactsDao db;
    private RemoteRepo remoteRepo;
    private SharedPrefs sharedPrefs;

    @Inject
    public Repository(ContactsDao db, RemoteRepo remoteRepo, SharedPrefs sharedPrefs) {
        this.db = db;
        this.remoteRepo = remoteRepo;
        this.sharedPrefs = sharedPrefs;
    }

    public Flowable<ArrayList<Person>> getData() {
        Observable<ArrayList<Person>> observable = needToRefresh() ? getApiData() : getLocalData();
        return observable
                .toFlowable(BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<ArrayList<Person>> getApiData() {
        return remoteRepo.getData()
                .doOnNext(people -> {
                    db.batchInsert(people);
                    sharedPrefs.saveTimeStamp(System.currentTimeMillis());
                });
    }

    private Observable<ArrayList<Person>> getLocalData() {
        return Observable.create(emitter -> emitter.onNext(db.getAll()))
                .map(object -> (ArrayList<Person>) object);
    }

    private boolean needToRefresh() {
        long currentTime = System.currentTimeMillis();
        long oldTime = sharedPrefs.getLoadTimestamp();
        return DateTimeUtils.diffMoreThan(oldTime, currentTime, DIFF_TO_REFRESH_IN_SECONDS);
    }
}
