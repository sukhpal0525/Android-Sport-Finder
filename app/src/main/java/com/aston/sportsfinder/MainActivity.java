package com.aston.sportsfinder;

import android.os.Bundle;
import android.util.Log;

import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.aston.sportsfinder.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseClient.getInstance(this);

        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            Log.d("TEST", "Navigated to: " + destination.getLabel());
        });
    }
}



//    private ActivityMainBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
////        BottomNavigationView navView = binding.navView;
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_search, R.id.navigation_profile)
//                .build();
//
////      NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
////      NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
////      NavigationUI.setupWithNavController(navView, navController);
//
//        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupWithNavController(bottomNav, navController);
//
//    }
//}