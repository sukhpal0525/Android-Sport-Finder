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
import androidx.navigation.fragment.NavHostFragment;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.api.RetrofitClient;
import com.aston.sportsfinder.api.WeatherResponse;
import com.aston.sportsfinder.api.WeatherService;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.Notification;
import com.aston.sportsfinder.model.viewmodel.game.GameViewModel;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class JoinGameBottomSheet extends BottomSheetDialogFragment {

    private Game game;
    private ExecutorService asyncTaskExecutor;
    private GameViewModel gamesViewModel;

    public static JoinGameBottomSheet newInstance(Game game) {
        JoinGameBottomSheet fragment = new JoinGameBottomSheet();
        fragment.game = game;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncTaskExecutor = DatabaseClient.getInstance(requireContext()).executorService;
        gamesViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
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
        Button btnCancel = view.findViewById(R.id.btnCancel);

        // Listeners
        ivClose.setOnClickListener(v -> dismiss());
        btnConfirm.setOnClickListener(v -> joinGame());
        btnCancel.setOnClickListener(v -> { dismiss();
        });
    }

    public void insertNotification(int userId) {
        asyncTaskExecutor.execute(() -> {
            String message = game.getTeam1() + " vs " + game.getTeam2() +
                    "\nDate: " + game.getDate() +
                    "\nTime: " + game.getTime();

            Notification notification = new Notification(0, userId, message, System.currentTimeMillis(), false, game.getId());
            NotificationsViewModel viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
            viewModel.insertNotifications(notification);
        });
    }

    public void joinGame() {
        asyncTaskExecutor.execute(() -> {
                Log.d("SSS", "Button clicked for game ID: " + game.getId());
                Integer userId = DatabaseClient.getInstance(getContext()).getAppDatabase().userDao().getCurrentUserId();
                if (userId != null && !game.isStarted()) {
                    processGameJoin(userId);
                } else {
                    Log.d("SSS", "Can't join, game already started/joined");
                }
            dismiss();
        });
    }

//    public void processGameJoin(int userId) {
//        GameDao gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
//        if (!gameDao.isGameJoined(selectedGame.getId(), userId)) {
//            gameDao.updateGameJoinStatus(selectedGame.getId(), true, userId);
//            insertNotification(userId);
//            showJoinSuccessBottomSheet();
//        } else {
//            showJoinFailBottomSheet();
//        }
//    }

    public void processGameJoin(int userId) {
        GameDao gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
        if (!gameDao.isGameJoined(game.getId(), userId)) {
            gameDao.updateGameJoinStatus(game.getId(), true, userId);
            List<Game> updatedGames = gameDao.getAllGames();
            getActivity().runOnUiThread(() -> gamesViewModel.updateGames(updatedGames));
            insertNotification(userId);
            showJoinSuccessBottomSheet();
        } else {
            showJoinFailBottomSheet();
        }
    }

    public void showJoinSuccessBottomSheet() {
        JoinSuccessBottomSheet bottomSheet = new JoinSuccessBottomSheet();
        bottomSheet.show(getParentFragmentManager(), "JoinSuccessBottomSheet");
    }

    public void showJoinFailBottomSheet() {
        JoinFailBottomSheet bottomSheet = new JoinFailBottomSheet();
        bottomSheet.show(getParentFragmentManager(), "JoinFailBottomSheet");
    }
}