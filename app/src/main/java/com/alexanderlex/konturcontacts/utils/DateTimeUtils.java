package com.alexanderlex.konturcontacts.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {
    public static String convertDate(String dateStr) {
        String result = "";

        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat formatOut = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = formatIn.parse(dateStr);
            result = formatOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean diffMoreThan(long oldTime, long newTime, long diffInSeconds) {
        long oldMinutes = TimeUnit.MILLISECONDS.toSeconds(oldTime);
        long newMinutes = TimeUnit.MILLISECONDS.toSeconds(newTime);

        long diff = newMinutes - oldMinutes;

        return diff > diffInSeconds;
    }
}
