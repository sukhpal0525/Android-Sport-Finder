package com.aston.sportsfinder.fragment.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.model.AppUser;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.Notification;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class JoinGameBottomSheet extends BottomSheetDialogFragment {

    private Game selectedGame;

    public static JoinGameBottomSheet newInstance(Game game) {
        JoinGameBottomSheet fragment = new JoinGameBottomSheet();
        fragment.selectedGame = game;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.join_game_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Close current bottom sheet
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> dismiss());

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            int userId = AppUser.USER_ID;
            DatabaseClient.getInstance(getContext()).executorService.execute(() -> {
                DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao()
                        .updateGameJoinStatus(selectedGame.getId(), true, userId);

                Notification notification = new Notification(
                        0, userId, "Joined game: " + selectedGame.getTeam1() + " vs " + selectedGame.getTeam2(),
                        System.currentTimeMillis(), false, selectedGame.getId());
                DatabaseClient.getInstance(getContext()).getAppDatabase().notificationDao()
                        .insertNotification(notification);
            });
            dismiss();
        });
    }
}