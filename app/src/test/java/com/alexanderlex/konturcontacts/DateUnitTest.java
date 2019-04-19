package com.alexanderlex.konturcontacts;

import com.alexanderlex.konturcontacts.utils.DateTimeUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateUnitTest {
    @Test
    public void dateConvert_isCorrect() {
        String dateStrIn = "2013-07-15T11:44:05-06:00";
        String dateStrOut = "15.07.2013";
        assertEquals(DateTimeUtils.convertDate(dateStrIn),dateStrOut);
    }
}