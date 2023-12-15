package com.aston.sportsfinder.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.api.WeatherData;
import com.aston.sportsfinder.api.WeatherResponse;
import com.aston.sportsfinder.fragment.search.SearchViewModel;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class HomeFragment extends Fragment {

    private TextView yourNextGameTitle;
    private TextView yourNextGameText;
    private TextView tvTeamNames;
    private TextView tvGameDetails;
    private TextView tvWeatherInfo;
    private ExecutorService asyncTaskExecutor;
    private SearchViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        asyncTaskExecutor = DatabaseClient.getInstance(getContext()).executorService;
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.navigation_search);
        });

        ImageView downArrow = view.findViewById(R.id.downArrow);
        downArrow.setColorFilter(Color.parseColor("#787878"));

        yourNextGameTitle = view.findViewById(R.id.yourNextGameTitle);
        yourNextGameText = view.findViewById(R.id.yourNextGameText);

        tvTeamNames = view.findViewById(R.id.tvTeamNames);
        tvGameDetails = view.findViewById(R.id.tvGameDetails);
        tvWeatherInfo = view.findViewById(R.id.tvWeatherInfo);

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        loadUpcomingGames();
    }

    public void loadUpcomingGames() {
        asyncTaskExecutor.execute(() -> {
            Integer userId = DatabaseClient.getInstance(getContext()).getAppDatabase().userDao().getCurrentUserId();
            if (userId != null) {
                List<Game> games = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao().getUpcomingGames(userId);
                getActivity().runOnUiThread(() -> checkForUpcomingGame(games));
            }
        });
    }

    private void fetchWeatherData(Game game) {
        viewModel.fetchWeatherData(game).observe(getViewLifecycleOwner(), weatherResponse -> {
            viewModel.displayWeatherInfo(weatherResponse, tvWeatherInfo);
        });
    }


    public void checkForUpcomingGame(List<Game> games) {
        if (games.isEmpty()) {
            yourNextGameText.setText("You don't have any upcoming games");
            tvTeamNames.setText("");
            tvGameDetails.setText("");
            tvWeatherInfo.setText("");
        } else {
            Game nextGame = games.get(0);
            yourNextGameText.setText("You have a game starting soon:");
            tvTeamNames.setText(nextGame.getTeam1() + " vs " + nextGame.getTeam2());
            String gameDetails =
                    "Type: " + nextGame.getGameType() + "\n" +
                            "Date: " + nextGame.getDate() + " (" + nextGame.getTime() + ")\n" +
                            "Location: " + nextGame.getStreet() + ", " + nextGame.getCity();
            tvGameDetails.setText(gameDetails);
            fetchWeatherData(nextGame);
        }
    }
}