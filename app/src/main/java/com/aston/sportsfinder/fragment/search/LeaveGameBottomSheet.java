package com.aston.sportsfinder.fragment.search;

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
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.dao.NotificationDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.ExecutorService;

public class LeaveGameBottomSheet extends BottomSheetDialogFragment {

    private Game selectedGame;
    private ExecutorService asyncTaskExecutor;
    private NotificationsViewModel viewModel;

    public static LeaveGameBottomSheet newInstance(Game game) {
        LeaveGameBottomSheet fragment = new LeaveGameBottomSheet();
        fragment.selectedGame = game;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncTaskExecutor = DatabaseClient.getInstance(requireContext()).executorService;
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_game_bottom_sheet, container, false);

        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        ImageView ivClose = view.findViewById(R.id.ivClose);

        ivClose.setOnClickListener(v -> dismiss());
        btnCancel.setOnClickListener(v -> dismiss());
        btnConfirm.setOnClickListener(v -> leaveGame());

        return view;
    }

    public void leaveGame() {
        asyncTaskExecutor.execute(() -> {
            Log.d("SSS", "Leaving game ID: " + selectedGame.getId());
            Integer userId = DatabaseClient.getInstance(getContext()).getAppDatabase().userDao().getCurrentUserId();
            GameDao gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
            NotificationDao notificationDao = DatabaseClient.getInstance(getContext()).getAppDatabase().notificationDao();
            if (gameDao.isGameJoined(selectedGame.getId(), userId)) {
                gameDao.updateGameJoinStatus(selectedGame.getId(), false, userId);
                notificationDao.removeNotification(userId, selectedGame.getId());
                showLeaveSuccessBottomSheet();
                viewModel.loadNotifications();
            } else {
                showLeaveFailBottomSheet();
            }
            dismiss();
        });
    }

    public void showLeaveSuccessBottomSheet() {
        LeaveSuccessBottomSheet bottomSheet = new LeaveSuccessBottomSheet();
        bottomSheet.show(getParentFragmentManager(), "LeaveSuccessBottomSheet");
    }

    public void showLeaveFailBottomSheet() {
        LeaveFailBottomSheet bottomSheet = new LeaveFailBottomSheet();
        bottomSheet.show(getParentFragmentManager(), "LeaveFailBottomSheet");
    }
}