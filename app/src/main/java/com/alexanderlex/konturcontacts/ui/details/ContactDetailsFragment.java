package com.alexanderlex.konturcontacts.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexanderlex.konturcontacts.R;
import com.alexanderlex.konturcontacts.data.models.Person;
import com.alexanderlex.konturcontacts.ui.SharedViewModel;
import com.alexanderlex.konturcontacts.utils.DateTimeUtils;

public class ContactDetailsFragment extends Fragment {

    private ContactDetailsViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private View rootView;
    private TextView nameTv;
    private TextView phoneTv;
    private TextView tempTv;
    private TextView eduTv;
    private TextView bioTv;

    public static ContactDetailsFragment newInstance() {
        return new ContactDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.details_fragment, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        nameTv = rootView.findViewById(R.id.name_tv);
        phoneTv = rootView.findViewById(R.id.phone_tv);
        tempTv = rootView.findViewById(R.id.temperament_tv);
        eduTv = rootView.findViewById(R.id.education_tv);
        bioTv = rootView.findViewById(R.id.bio_tv);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ContactDetailsViewModel.class);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        sharedViewModel.getSelected().observe(this, this::showPerson);
    }

    private void showPerson(Person person) {
        if (person != null) {
            String educationStart = DateTimeUtils.convertDate(person.getEducationPeriod().getStart());
            String educationEnd = DateTimeUtils.convertDate(person.getEducationPeriod().getEnd());
            String educationStr = String.format("%s - %s", educationStart, educationEnd);

            nameTv.setText(person.getName());
            phoneTv.setText(person.getPhone());
            bioTv.setText(person.getBiography());
            tempTv.setText(person.getTemperament());
            eduTv.setText(educationStr);

            phoneTv.setOnClickListener(v -> {
                openDialer(person.getPhone());
            });
        }
    }

    private void openDialer(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}
