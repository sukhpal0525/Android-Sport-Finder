package com.aston.sportsfinder.fragment.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;

import java.util.concurrent.ExecutorService;

public class NotificationDetailsFragment extends Fragment {

    private NotificationsViewModel viewModel;
    private GameDao gameDao;
    private ExecutorService asyncTaskExecutor;
    private Game currentGame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
        gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
        asyncTaskExecutor = DatabaseClient.getInstance(requireContext()).executorService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_details, container, false);

        viewModel.getSelectedGameId().observe(getViewLifecycleOwner(), gameId -> {
            asyncTaskExecutor.execute(() -> {
                Game game = gameDao.getGameById(gameId);
                currentGame = game;
                getActivity().runOnUiThread(() -> updateUIWithGameDetails(game));
            });
        });
        return view;
    }

    private void updateUIWithGameDetails(Game game) {
        TextView textViewGameType = getView().findViewById(R.id.tvGameType);
        TextView textViewTeams = getView().findViewById(R.id.tvTeams);
        TextView textViewDateTime = getView().findViewById(R.id.tvDateTime);
        TextView textViewLocation = getView().findViewById(R.id.tvLocation);

        textViewGameType.setText("Type: " + game.getGameType());
        textViewTeams.setText(game.getTeam1() + " vs " + game.getTeam2());
        textViewDateTime.setText("Date: " + game.getDate() + "\nTime: " + game.getTime());
        textViewLocation.setText("Location: " + game.getStreet() + ", " + game.getCity());
    }
}