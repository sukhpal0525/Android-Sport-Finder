package com.aston.sportsfinder.fragment.search;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.model.Game;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class GameDetailsBottomSheet extends BottomSheetDialogFragment {

    private Game game;

    public static GameDetailsBottomSheet newInstance(Game game) {
        GameDetailsBottomSheet fragment = new GameDetailsBottomSheet();
        fragment.game = game;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_game_details, container, false);

        TextView tvGameDetails = view.findViewById(R.id.tvGameDetails);
        TextView tvTeamInfo = view.findViewById(R.id.tvTeamInfo);
        TextView tvScore = view.findViewById(R.id.tvScore);
        TextView tvAdditionalInfo = view.findViewById(R.id.tvAdditionalInfo);
        TextView tvStatus = view.findViewById(R.id.tvStatus);
        Button btnAction = view.findViewById(R.id.btnAction);

        tvTeamInfo.setText(game.getTeam1() + " vs " + game.getTeam2());
        tvScore.setText("Score: " + game.getScore1() + " - " + game.getScore2());
        tvGameDetails.setText("Location: " + game.getStreet() + ", " + game.getCity() + "\nDate: " + game.getDate() + "\nTime: " + game.getTime());
        tvAdditionalInfo.setText("Additional Info: Weather, Audience, etc.");
        String scoreText = game.isStarted() ? "Score: " + game.getScore1() + " - " + game.getScore2() : "Not started";
        tvScore.setText(scoreText);

        if (game.isStarted()) {
            tvStatus.setText("Status: Ended");
            btnAction.setText("Ended");
            btnAction.setEnabled(false);
            btnAction.setBackgroundColor(Color.GRAY);
            btnAction.setTextColor(Color.DKGRAY);
        } else {
            tvStatus.setText("Status: Not started");
            btnAction.setText("Join Game");
            btnAction.setEnabled(true);
            btnAction.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_light));
        }

        if (!game.isStarted()) {
            btnAction.setOnClickListener(v -> {
                dismiss();
                JoinGameBottomSheet joinGameBottomSheet = JoinGameBottomSheet.newInstance();
                joinGameBottomSheet.show(getParentFragmentManager(), "JoinGameBottomSheet");
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Close current bottom sheet
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> dismiss());
    }
}
