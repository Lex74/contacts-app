package com.alexanderlex.konturcontacts.data;

import com.alexanderlex.konturcontacts.BuildConfig;
import com.alexanderlex.konturcontacts.data.models.Person;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteRepo {
    private static final String BASE_URL = "https://raw.githubusercontent.com";
    private KonturApi konturApi;

    @Inject
    public RemoteRepo() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel((BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BASIC : HttpLoggingInterceptor.Level.NONE)))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        konturApi = retrofit.create(KonturApi.class);
    }

    public Observable<ArrayList<Person>> getData() {
        Observable<ArrayList<Person>> data1 = konturApi.getData(1);
        Observable<ArrayList<Person>> data2 = konturApi.getData(2);
        Observable<ArrayList<Person>> data3 = konturApi.getData(3);

        return Observable
                .zip(data1, data2, data3, this::sumData);
    }

    private ArrayList<Person> sumData(ArrayList<Person> persons, ArrayList<Person> persons2, ArrayList<Person> persons3) {
        persons.addAll(persons2);
        persons.addAll(persons3);

        return persons;
    }
}
