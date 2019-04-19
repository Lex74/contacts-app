package com.alexanderlex.konturcontacts.utils;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.alexanderlex.konturcontacts.data.models.Person;

import java.util.ArrayList;

public class PersonDiffUtil {
    public void applyDiff(@NonNull ArrayList<Person> oldList, @ NonNull ArrayList<Person> newList,
                          RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldIndex, int newIndex) {
                return oldList.get(oldIndex).getId().equals(newList.get(newIndex).getId());
            }

            @Override
            public boolean areContentsTheSame(int oldIndex, int newIndex) {
                Person oldItem = oldList.get(oldIndex);
                Person newItem = newList.get(newIndex);
                return oldItem.getName().equals(newItem.getName()) &&
                        oldItem.getPhone().equals(newItem.getPhone()) &&
                        oldItem.getHeight().equals(newItem.getHeight());
            }
        });

        diffResult.dispatchUpdatesTo(adapter);
    }
}
