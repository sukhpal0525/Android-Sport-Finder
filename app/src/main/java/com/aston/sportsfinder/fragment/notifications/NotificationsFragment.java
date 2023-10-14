package com.aston.sportsfinder.fragment.nav.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aston.sportsfinder.databinding.FragmentDashboardBinding;
import com.aston.sportsfinder.databinding.FragmentHomeBinding;
import com.aston.sportsfinder.databinding.FragmentNotificationsBinding;
import com.aston.sportsfinder.model.dashboard.DashboardViewModel;
import com.aston.sportsfinder.model.home.HomeViewModel;
import com.aston.sportsfinder.model.notifications.NotificationsViewModel;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.welcomeMessage;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}