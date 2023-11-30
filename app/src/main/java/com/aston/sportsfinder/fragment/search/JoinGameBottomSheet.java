package com.aston.sportsfinder.fragment.search;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.api.RetrofitClient;
import com.aston.sportsfinder.api.WeatherResponse;
import com.aston.sportsfinder.api.WeatherService;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.Notification;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.ExecutorService;

public class JoinGameBottomSheet extends BottomSheetDialogFragment {

    private Game selectedGame;
    private ExecutorService asyncTaskExecutor;

    public static JoinGameBottomSheet newInstance(Game game) {
        JoinGameBottomSheet fragment = new JoinGameBottomSheet();
        fragment.selectedGame = game;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncTaskExecutor = DatabaseClient.getInstance(requireContext()).executorService;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.join_game_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivClose = view.findViewById(R.id.ivClose);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        // Listeners
        ivClose.setOnClickListener(v -> dismiss());
        btnConfirm.setOnClickListener(v -> joinGame());
    }

    private void insertNotification(int userId) {
        asyncTaskExecutor.execute(() -> {
            String message = "Joined game: " + selectedGame.getTeam1() + " vs " + selectedGame.getTeam2() +
                    "\nDate: " + selectedGame.getDate() +
                    "\nTime: " + selectedGame.getTime();

            Notification notification = new Notification(0, userId, message, System.currentTimeMillis(), false, selectedGame.getId());
            DatabaseClient.getInstance(getContext()).getAppDatabase().notificationDao().insertNotification(notification);
            Log.d("SSS", "Notification created for game ID: " + selectedGame.getId());

            NotificationsViewModel viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
            viewModel.refreshNotifications();
        });
    }

    private void joinGame() {
        asyncTaskExecutor.execute(() -> {
            try {
                Log.d("SSS", "Button clicked for game ID: " + selectedGame.getId());
                Integer userId = DatabaseClient.getInstance(getContext()).getAppDatabase().userDao().getCurrentUserId();

                if (userId != null && !selectedGame.isStarted()) {
                    processGameJoin(userId);
                } else {
                    notify("Cannot join, game already started or User ID is null");
                }
            } catch (Exception e) {
                Log.e("SSS", "Error joining game", e);
                notify("Error joining game");
            }
            dismiss();
        });
    }

    private void processGameJoin(int userId) {
        GameDao gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
        if (!gameDao.isGameJoinedByUser(selectedGame.getId(), userId)) {
            gameDao.updateGameJoinStatus(selectedGame.getId(), true, userId);
            insertNotification(userId);
            notify("Joined game successfully!");
        } else {
            notify("You have already joined this game.");
        }
    }

    private void notify(String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }
}