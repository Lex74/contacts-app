package com.alexanderlex.konturcontacts.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

public class SharedPrefs {
    private static final String LOAD_TIMESTAMP = "load_timestamp";
    private Context context;
    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPrefs(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveTimeStamp(long time) {
        sharedPreferences.edit().putLong(LOAD_TIMESTAMP, time).apply();
    }

    public long getLoadTimestamp() {
        return sharedPreferences.getLong(LOAD_TIMESTAMP, 0);
    }
}
