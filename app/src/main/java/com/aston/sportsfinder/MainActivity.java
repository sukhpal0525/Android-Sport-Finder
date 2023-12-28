package com.aston.sportsfinder;

import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.aston.sportsfinder.model.Notification;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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

        // Explicitly handle navigation for bottom nav
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home || itemId == R.id.navigation_search ||
                    itemId == R.id.navigation_notifications || itemId == R.id.navigation_help) {
                Log.d("SSS", "Navigated to: " + item.getTitle());
                navController.navigate(itemId);
                return true;
            }
            return false;
        });

        // Change action bar title based on the current fragment's name
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            String label = destination.getLabel().toString();
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(label);
        });

        NotificationsViewModel viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        viewModel.getUnreadNotificationsCount().observe(this, this::updateNotificationsBadge);
    }

    public void updateNotificationsBadge(int notificationAmount) {
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.navigation_notifications);
        if (notificationAmount > 0) {
            badge.setVisible(true);
            badge.setNumber(notificationAmount);
        } else {
            badge.setVisible(false);
        }
    }
}