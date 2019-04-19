package com.alexanderlex.konturcontacts.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexanderlex.konturcontacts.R;
import com.alexanderlex.konturcontacts.data.models.Person;
import com.alexanderlex.konturcontacts.utils.PersonDiffUtil;

import java.util.ArrayList;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonViewHolder> {
    private ArrayList<Person> persons;
    private OnItemClickListener onItemClickListener;

    PersonsAdapter(OnItemClickListener onItemClickListener) {
        this.persons = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact, viewGroup, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder personViewHolder, int i) {
        personViewHolder.bind(persons.get(i));
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    void updateData(ArrayList<Person> filteredList) {
        new PersonDiffUtil().applyDiff(persons, filteredList, this);
        persons = filteredList;
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        private TextView nameTv;
        private TextView phoneTv;
        private TextView heightTv;

        PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;
            nameTv = itemView.findViewById(R.id.name_tv);
            phoneTv = itemView.findViewById(R.id.phone_tv);
            heightTv = itemView.findViewById(R.id.height_tv);
        }

        void bind(Person person) {
            nameTv.setText(person.getName());
            phoneTv.setText(person.getPhone());
            heightTv.setText(String.valueOf(person.getHeight()));

            rootView.setOnClickListener((v) -> onItemClickListener.onItemClick(person));
        }
    }

    interface OnItemClickListener {
        void onItemClick(Person person);
    }
}
