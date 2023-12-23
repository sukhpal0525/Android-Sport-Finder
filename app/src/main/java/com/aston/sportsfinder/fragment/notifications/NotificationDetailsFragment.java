package com.aston.sportsfinder.fragment.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.fragment.search.LeaveGameBottomSheet;
import com.aston.sportsfinder.fragment.search.SearchViewModel;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;

import java.util.concurrent.ExecutorService;

public class NotificationDetailsFragment extends Fragment {

    private NotificationsViewModel viewModel;
    private GameDao gameDao;
    private ExecutorService asyncTaskExecutor;
    private Game currentGame;
    private SearchViewModel searchViewModel;
    private TextView tvWeatherInfo;

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
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        Button leaveGameButton = view.findViewById(R.id.buttonLeaveGame);

        leaveGameButton.setOnClickListener(v -> {
                LeaveGameBottomSheet bottomSheet = LeaveGameBottomSheet.newInstance(currentGame);
                bottomSheet.show(getParentFragmentManager(), "LeaveGameBottomSheet");
        });

        viewModel.getSelectedGameId().observe(getViewLifecycleOwner(), gameId -> {
            asyncTaskExecutor.execute(() -> {
                Game game = gameDao.getGameById(gameId);
                currentGame = game;
                getActivity().runOnUiThread(() -> updateUIWithGameDetails(game));
            });
        });
        return view;
    }

    private void fetchWeatherData(Game game) {
        searchViewModel.fetchWeatherData(game).observe(getViewLifecycleOwner(), weatherResponse ->
                searchViewModel.displayDetailedWeatherInfo(weatherResponse, tvWeatherInfo));
    }

    public void updateUIWithGameDetails(Game game) {
        TextView textViewGameType = getView().findViewById(R.id.tvGameType);
        TextView textViewTeams = getView().findViewById(R.id.tvTeams);
        TextView textViewDateTime = getView().findViewById(R.id.tvDateTime);
        TextView textViewLocation = getView().findViewById(R.id.tvLocation);
        TextView textViewOrganiserName = getView().findViewById(R.id.tvOrganiserName);
        TextView textViewCapacity = getView().findViewById(R.id.tvCapacity);
        TextView textViewSkillLevel = getView().findViewById(R.id.tvSkillLevel);
        TextView textViewEquipmentNeeded = getView().findViewById(R.id.tvEquipmentNeeded);
        TextView textViewDuration = getView().findViewById(R.id.tvDuration);
        tvWeatherInfo = getView().findViewById(R.id.tvWeatherInfo);
        TextView textViewAgeGroup = getView().findViewById(R.id.tvAgeGroup);
        TextView textViewRegistrationFee = getView().findViewById(R.id.tvRegistrationFee);
        TextView textViewAdditionalNotes = getView().findViewById(R.id.tvAdditionalNotes);

        textViewGameType.setText("Type: " + game.getGameType());
        textViewTeams.setText(game.getTeam1() + " vs " + game.getTeam2());
        textViewDateTime.setText("Date: " + game.getDate() + "\nTime: " + game.getTime());
        textViewLocation.setText("Location: " + game.getStreet() + ", " + game.getCity());
        textViewOrganiserName.setText("Organiser: " + game.getOrganiserName());
        textViewCapacity.setText("Capacity: " + game.getCapacity() + " (Current: " + game.getCurrentPlayerCount() + ")");
        textViewSkillLevel.setText("Skill Level: " + game.getSkillLevel());
        textViewEquipmentNeeded.setText("Equipment Needed: " + game.getEquipmentNeeded());
        textViewDuration.setText("Duration: " + game.getDuration());
        fetchWeatherData(game);
        textViewAgeGroup.setText("Age Group: " + game.getAgeGroup());
        textViewRegistrationFee.setText("Registration Fee: " + "£" + game.getFormatRegistrationFee());
        textViewAdditionalNotes.setText("Notes: " + game.getAdditionalNotes());
    }
}