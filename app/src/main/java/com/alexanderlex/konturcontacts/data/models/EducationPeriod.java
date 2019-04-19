package com.alexanderlex.konturcontacts.data.models;

import android.arch.persistence.room.Entity;

@Entity
public class EducationPeriod {
    private String start;
    private String end;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

}
