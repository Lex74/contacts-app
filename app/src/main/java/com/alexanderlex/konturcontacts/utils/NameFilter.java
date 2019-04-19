package com.alexanderlex.konturcontacts.utils;

import android.widget.Filter;

import com.alexanderlex.konturcontacts.data.models.Person;

import java.util.ArrayList;

public class NameFilter extends Filter {
    private ArrayList<Person> data;
    private FilterListener filterListener;

    public NameFilter(ArrayList<Person> data, FilterListener filterListener) {
        this.data = data;
        this.filterListener = filterListener;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (data == null) {
            return results;
        }

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toLowerCase();
            ArrayList<Person> filteredList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getName().toLowerCase().contains(constraint)) {
                    filteredList.add(data.get(i));
                }
            }

            results.count = filteredList.size();
            results.values = filteredList;
        } else {
            results.count = data.size();
            results.values = data;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        filterListener.onResult((ArrayList<Person>) results.values);
    }

    public interface FilterListener {
        void onResult(ArrayList<Person> filteredList);
    }
}
