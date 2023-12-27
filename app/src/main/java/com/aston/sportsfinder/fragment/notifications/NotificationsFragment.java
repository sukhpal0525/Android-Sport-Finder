package com.aston.sportsfinder.fragment.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.dao.NotificationDao;
import com.aston.sportsfinder.databinding.FragmentNotificationsBinding;
import com.aston.sportsfinder.fragment.search.LeaveGameBottomSheet;
import com.aston.sportsfinder.fragment.search.SearchErrorBottomSheet;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.Notification;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsAdapter;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private ExecutorService asyncTaskExecutor;
    private NotificationDao notificationDao;
    private NotificationsAdapter adapter;
    private NotificationsViewModel viewModel;
    private TextView tvNoNotifications;
    private boolean isListView = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
        tvNoNotifications = binding.getRoot().findViewById(R.id.tvNoNotifications);
        asyncTaskExecutor = DatabaseClient.getInstance(getContext()).executorService;
        GameDao gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
        notificationDao = DatabaseClient.getInstance(getContext()).getAppDatabase().notificationDao();

        adapter = new NotificationsAdapter(gameDao, asyncTaskExecutor, new NotificationsAdapter.OnNotificationClickListener() {
            @Override
            public void onNotificationClick(int gameId) {
                viewModel.selectGameId(gameId);
                NavHostFragment.findNavController(NotificationsFragment.this)
                        .navigate(R.id.navigation_notification_details);
            }
            @Override
            public void onLeaveGameClicked(Notification notification) {
                asyncTaskExecutor.execute(() -> {
                    Game game = gameDao.getGameById(notification.getGameId());
                        showLeaveGameBottomSheet(game);
                });
            }
        });

        Button btnCardView = binding.btnCardView;
        Button btnListView = binding.btnListView;

        btnCardView.setOnClickListener(v -> setViewType(false));
        btnListView.setOnClickListener(v -> setViewType(true));

        binding.notificationsRecyclerView.setAdapter(adapter);
        //This is just so the recycler view knows how to position the notification items
        binding.notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Button btnReset = view.findViewById(R.id.btnReset);
//        btnReset.setOnClickListener(v -> { NavHostFragment.findNavController(this).navigate(R.id.navigation_notifications);
//        });

        LinearLayout btnReset = view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.navigation_notifications);
        });

        Button btnNoNotifications = view.findViewById(R.id.btnNoNotifications);
        btnNoNotifications.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.navigation_search);
        });

        viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            Log.d("SSS", "Notifications received: " + notifications.size());
            if (notifications != null) {
                adapter.setNotifications(notifications);
                tvNoNotifications.setVisibility(notifications.isEmpty() ? View.VISIBLE : View.GONE);
                btnNoNotifications.setVisibility(notifications.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        EditText searchBar = getView().findViewById(R.id.searchBar);
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchQuery = searchBar.getText().toString().toLowerCase();
                filterNotifications(searchQuery);
                return true;
            }
            return false;
        });
    }

    public void filterNotifications(String query) {
        asyncTaskExecutor.execute(() -> {
            Integer userId = DatabaseClient.getInstance(getContext()).getAppDatabase().userDao().getCurrentUserId();
            if (userId != null) {
                List<Notification> filteredNotifications = notificationDao.searchForNotification(userId, query.toLowerCase());
                getActivity().runOnUiThread(() -> {
                    if (filteredNotifications.isEmpty()) {
                        showErrorBottomSheet();
                    } else {
                        adapter.setNotifications(filteredNotifications);
                    }
                });
            }
        });
    }

    private void showErrorBottomSheet() {
        SearchErrorBottomSheet bottomSheet = SearchErrorBottomSheet.newInstance();
        bottomSheet.show(getChildFragmentManager(), "SearchErrorBottomSheet");
    }

    private void showLeaveGameBottomSheet(Game game) {
        LeaveGameBottomSheet leaveGameBottomSheet = LeaveGameBottomSheet.newInstance(game);
        leaveGameBottomSheet.show(getChildFragmentManager(), "LeaveGameBottomSheet");
    }

    public void setViewType(boolean listView) {
        isListView = listView;
        if (adapter != null) {
            adapter.setListView(isListView);
        }
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