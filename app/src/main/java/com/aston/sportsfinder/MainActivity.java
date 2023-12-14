package com.aston.sportsfinder;

import android.os.Bundle;
import android.util.Log;

import com.aston.sportsfinder.model.Notification;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;
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

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            String label = destination.getLabel().toString();
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(label);
            Log.d("SSS", "Navigated to: " + label);
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