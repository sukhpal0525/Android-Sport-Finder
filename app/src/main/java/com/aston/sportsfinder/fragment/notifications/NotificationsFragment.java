package com.aston.sportsfinder.fragment.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.databinding.FragmentNotificationsBinding;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsAdapter;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationsAdapter adapter;
    private NotificationsViewModel viewModel;
    private TextView tvNoNotifications;
    private Button btnFindMatch;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
        tvNoNotifications = binding.getRoot().findViewById(R.id.tvNoNotifications);
        ExecutorService asyncTaskExecutor = DatabaseClient.getInstance(getContext()).executorService;
        GameDao gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();

        adapter = new NotificationsAdapter(gameDao, asyncTaskExecutor, gameId -> {
            viewModel.selectGameId(gameId);
            NavHostFragment.findNavController(NotificationsFragment.this)
                    .navigate(R.id.navigation_notification_details);
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
                tvNoNotifications.setVisibility(View.GONE);
            } else {
                tvNoNotifications.setVisibility(View.VISIBLE);
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