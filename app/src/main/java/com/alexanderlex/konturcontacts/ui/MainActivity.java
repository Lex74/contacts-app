package com.alexanderlex.konturcontacts.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alexanderlex.konturcontacts.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private SharedViewModel sharedViewModel;

    @Override
    public void onBackPressed() {
        sharedViewModel.select(null);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        sharedViewModel.getSelected().observe(this, person -> {
            if (person != null && currentNavDestinationIsMain()) {
                navController.navigate(R.id.action_mainFragment_to_contactDetailsFragment);
            }
        });
    }

    private boolean currentNavDestinationIsMain(){
        return navController.getCurrentDestination().getId() == R.id.mainFragment;
    }
}
