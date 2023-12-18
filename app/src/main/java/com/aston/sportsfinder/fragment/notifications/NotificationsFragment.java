package com.aston.sportsfinder.fragment.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.databinding.FragmentNotificationsBinding;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsAdapter;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationsAdapter adapter;
    private NotificationsViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);

        adapter = new NotificationsAdapter(gameId -> {
            viewModel.selectGameId(gameId);
            NavHostFragment.findNavController(this).navigate(R.id.navigation_notification_details);
        });

        binding.notificationsRecyclerView.setAdapter(adapter);
        //This is just so the recycler view knows how to position the notification items
        binding.notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            Log.d("SSS", "Notifications received: " + notifications.size());
            if (notifications != null && !notifications.isEmpty()) {
                adapter.setNotifications(notifications);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadNotifications();
        viewModel.markNotificationsAsRead();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}