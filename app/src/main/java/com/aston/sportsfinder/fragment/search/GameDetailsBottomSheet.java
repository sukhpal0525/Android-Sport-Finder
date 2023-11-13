package com.aston.sportsfinder.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_game_details, container, false);

        TextView tvGameDetails = view.findViewById(R.id.tvGameDetails);
        TextView tvTeamInfo = view.findViewById(R.id.tvTeamInfo);
        TextView tvScore = view.findViewById(R.id.tvScore);
        TextView tvAdditionalInfo = view.findViewById(R.id.tvAdditionalInfo);


        tvTeamInfo.setText(game.getTeam1() + " vs " + game.getTeam2());
        tvScore.setText("Score: " + game.getScore1() + " - " + game.getScore2());
        tvGameDetails.setText("Location: " + game.getStreet() + ", " + game.getCity() + "\nDate: " + game.getDate() + "\nTime: " + game.getTime());
        tvAdditionalInfo.setText("Additional Info: Weather, Audience, etc.");

        return view;
    }
}