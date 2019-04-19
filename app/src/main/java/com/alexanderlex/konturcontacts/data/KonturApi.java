package com.alexanderlex.konturcontacts.data;

import android.support.annotation.IntRange;

import com.alexanderlex.konturcontacts.data.models.Person;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface KonturApi {

    @GET("/SkbkonturMobile/mobile-test-droid/master/json/generated-0{n}.json")
    public Observable<ArrayList<Person>> getData(@IntRange(from = 1, to = 3) @Path("n") int n);
}
