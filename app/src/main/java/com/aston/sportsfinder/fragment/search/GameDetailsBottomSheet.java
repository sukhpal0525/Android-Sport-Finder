package com.aston.sportsfinder.fragment.search;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.api.WeatherData;
import com.aston.sportsfinder.api.WeatherResponse;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.fragment.game.GameDeleteBottomSheet;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.ExecutorService;

public class GameDetailsBottomSheet extends BottomSheetDialogFragment {

    private Game game;
    private SearchViewModel viewModel;
    ImageButton btnEditGame, btnDeleteGame;

    public static GameDetailsBottomSheet newInstance(Game game) {
        GameDetailsBottomSheet fragment = new GameDetailsBottomSheet();
        fragment.game = game;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_game_details, container, false);
        btnEditGame = view.findViewById(R.id.btnEditGame);
        btnDeleteGame = view.findViewById(R.id.btnDeleteGame);


        TextView tvGameType = view.findViewById(R.id.tvGameType);
        TextView tvGameDetails = view.findViewById(R.id.tvGameDetails);
        TextView tvTeamInfo = view.findViewById(R.id.tvTeamInfo);
        TextView tvScore = view.findViewById(R.id.tvScore);
        TextView tvWeatherInfo = view.findViewById(R.id.tvWeatherInfo);
        TextView tvStatus = view.findViewById(R.id.tvStatus);
        Button btnAction = view.findViewById(R.id.btnAction);

        tvGameType.setText("Sport: " + game.getGameType());
        tvTeamInfo.setText(game.getTeam1() + " vs " + game.getTeam2());
        tvScore.setText("(Score: " + game.getScore1() + " - " + game.getScore2() + ")");
        tvGameDetails.setText("Location: " + game.getStreet() + ", " + game.getCity() + "\nDate: " + game.getDate() + "\nTime: " + game.getTime());
//        tvWeatherInfo.setText("Temperature:\nCondition:\nDescription:");
        String scoreText = game.isStarted() ? "Score: " + game.getScore1() + " - " + game.getScore2() : "(Not started)";
        tvScore.setText(scoreText);

        // update join button based on game isJoined and isStarted
        if (game.isStarted()) {
            tvStatus.setText("Status: Ended");
            btnAction.setText("Ended");
            btnAction.setEnabled(false);
            btnAction.setBackgroundColor(Color.GRAY);
            btnAction.setTextColor(Color.DKGRAY);
        } else if (game.isJoined()) {
            tvStatus.setText("Status: Joined");
            btnAction.setText("Already Joined");
            btnAction.setEnabled(false);
            btnAction.setBackgroundColor(Color.GRAY);
            btnAction.setTextColor(Color.DKGRAY);
        } else {
            tvStatus.setText("Status: Not started");
            btnAction.setText("Join Game");
            btnAction.setEnabled(true);
            btnAction.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_light));
        }

        Log.d("SSS", "IsCreatedByUser: " + game.isCreatedByUser());
        if (game.isCreatedByUser() == true) {
            btnEditGame.setVisibility(View.VISIBLE);
            btnDeleteGame.setVisibility(View.VISIBLE);
        } else {
            btnEditGame.setVisibility(View.GONE);
            btnDeleteGame.setVisibility(View.GONE);
        }

//        if (game.isCreatedByUser()) {
//            Button btnEditGame = view.findViewById(R.id.btnEditGame);
//            Button btnDeleteGame = view.findViewById(R.id.btnDeleteGame);
//
//            btnEditGame.setVisibility(View.VISIBLE);
//            btnDeleteGame.setVisibility(View.VISIBLE);
//            btnEditGame.setOnClickListener(v -> {
//            });
//            btnDeleteGame.setOnClickListener(v -> {
//            });
//        }

//         Listener for game joining
        if (!game.isStarted()) {
            btnAction.setOnClickListener(v -> {
                dismiss();
                JoinGameBottomSheet joinGameBottomSheet = JoinGameBottomSheet.newInstance(game);
                joinGameBottomSheet.show(getParentFragmentManager(), "JoinGameBottomSheet");
            });
        }

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.fetchWeatherData(game).observe(getViewLifecycleOwner(), weatherResponse ->
                viewModel.displayWeatherInfo(weatherResponse, view.findViewById(R.id.tvWeatherInfo)));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Close bottom sheet
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> dismiss());

        btnDeleteGame.setOnClickListener(v -> {
            dismiss();
            GameDeleteBottomSheet deleteBottomSheet = GameDeleteBottomSheet.newInstance(game);
            deleteBottomSheet.show(getParentFragmentManager(), "GameDeleteBottomSheet");
        });
    }
}