package com.alexanderlex.konturcontacts.ui.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.alexanderlex.konturcontacts.App;
import com.alexanderlex.konturcontacts.R;
import com.alexanderlex.konturcontacts.data.models.Person;
import com.alexanderlex.konturcontacts.ui.SharedViewModel;
import com.alexanderlex.konturcontacts.utils.ViewModelFactory;

import java.util.ArrayList;

import javax.inject.Inject;

public class MainFragment extends Fragment implements PersonsAdapter.OnItemClickListener {

    @Inject
    ViewModelFactory viewModelFactory;
    private MainViewModel viewModel;
    private SearchView personSearchView;
    private RecyclerView personListRv;
    private ProgressBar loadingProgressBar;
    private SharedViewModel sharedViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private PersonsAdapter personListAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        personSearchView = rootView.findViewById(R.id.person_search);
        personListRv = rootView.findViewById(R.id.person_list_rv);
        loadingProgressBar = rootView.findViewById(R.id.progress_bar);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_layout);

        return rootView;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((App) getActivity().getApplication()).getAppComponent().inject(this);

        Toolbar appBarLayout = getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(appBarLayout);
        appBarLayout.requestFocus();

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.refreshData());

        personListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        personListRv.setItemAnimator(new DefaultItemAnimator());
        personListRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        personListAdapter = new PersonsAdapter(this);
        personListRv.setAdapter(personListAdapter);

        setupSearchView();

        viewModel.exceptionData().observe(this, this::showErrorMessage);
        viewModel.personsData().observe(this, this::showData);

        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    void showData(ArrayList<Person> dataList) {
        if (dataList != null) {
            loadingProgressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            personListAdapter.updateData(dataList);
        }
    }

    void showErrorMessage(Throwable t) {
        t.printStackTrace();
        String stringFormat = getResources().getString(R.string.error_message);
        String message = String.format(stringFormat, t.getMessage());
        Snackbar.make(personListRv, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupSearchView() {
        int searchPlateId = personSearchView.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        personSearchView.findViewById(searchPlateId).setBackground(null);

        personSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                viewModel.filterData(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                viewModel.filterData(s);
                return true;
            }
        });
    }

    @Override
    public void onItemClick(Person person) {
        sharedViewModel.select(person);
    }
}
