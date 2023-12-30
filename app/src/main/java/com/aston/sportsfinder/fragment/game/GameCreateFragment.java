package com.aston.sportsfinder.fragment.game;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.fragment.search.SearchErrorBottomSheet;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.viewmodel.game.GameViewModel;
import com.aston.sportsfinder.util.DatabaseClient;

import java.util.concurrent.ExecutorService;

public class GameCreateFragment extends Fragment {

    private GameDao gameDao;
    private ExecutorService asyncTaskExecutor;
    private GameViewModel gameViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
        asyncTaskExecutor = DatabaseClient.getInstance(requireContext()).executorService;
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        EditText etGameType = view.findViewById(R.id.etGameType);
        EditText etTeam1 = view.findViewById(R.id.etTeam1);
        EditText etTeam2 = view.findViewById(R.id.etTeam2);
        EditText etCapacity = view.findViewById(R.id.etCapacity);
        EditText etDate = view.findViewById(R.id.etDate);
        EditText etTime = view.findViewById(R.id.etTime);
        EditText etLocation = view.findViewById(R.id.etLocation);
        EditText etLatitude = view.findViewById(R.id.etLatitude);
        EditText etLongitude = view.findViewById(R.id.etLongitude);
        EditText etDuration = view.findViewById(R.id.etDuration);
        EditText etOrganiserName = view.findViewById(R.id.etOrganiserName);
        EditText etSkillLevel = view.findViewById(R.id.etSkillLevel);
        EditText etEquipmentNeeded = view.findViewById(R.id.etEquipmentNeeded);
        EditText etRegistrationFee = view.findViewById(R.id.etRegistrationFee);
        EditText etAdditionalNotes = view.findViewById(R.id.etAdditionalNotes);

        Button saveButton = view.findViewById(R.id.saveGame);
        saveButton.setOnClickListener(v -> {
            String gameType = etGameType.getText().toString();
            String team1 = etTeam1.getText().toString();
            String team2 = etTeam2.getText().toString();

            String capacity = etCapacity.getText().toString();
            String date = etDate.getText().toString();
            String time = etTime.getText().toString();

            String latitudeString = etLatitude.getText().toString();
            String longitudeString = etLongitude.getText().toString();
            if (latitudeString.equals("") || longitudeString.equals("")) {
                Log.e("SSS", "Latitude and longitude need values");
                showGameCreateFailBottomSheet();
                return;
            }
            double latitude = 0.0;
            double longitude = 0.0;
            String location = etLocation.getText().toString();

            latitude = Double.parseDouble(etLatitude.getText().toString());
            longitude = Double.parseDouble(etLongitude.getText().toString());


            String duration = etDuration.getText().toString();
            String organiserName = etOrganiserName.getText().toString();
            String skillLevel = etSkillLevel.getText().toString();

            String equipmentNeeded = etEquipmentNeeded.getText().toString();
            String registrationFee = etRegistrationFee.getText().toString();
            String additionalNotes = etAdditionalNotes.getText().toString();


            Game newGame = new Game(gameType, team1, team2, 0, 0, false, false, "", "", "", "", latitude, longitude, date, time, organiserName, capacity, 0, skillLevel, equipmentNeeded, duration, registrationFee, additionalNotes, true);
            asyncTaskExecutor.execute(() -> {
                gameViewModel.setNewGame(newGame);
                gameDao.insertGame(newGame);
                getActivity().runOnUiThread(() -> {
                    showGameCreateSuccessBottomSheet();
                });
            });
        });

        Button selectLocationButton = view.findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(v -> {
            gameViewModel.setLocationSelectionMode(true);
            NavHostFragment.findNavController(this).navigate(R.id.navigation_search);
        });
        gameViewModel.getSelectedLocation().observe(getViewLifecycleOwner(), latLng -> {
            if (latLng != null) {
                etLatitude.setText(String.valueOf(latLng.latitude));
                etLongitude.setText(String.valueOf(latLng.longitude));

                Log.d("SSS", "Selected location - Latitude: " + latLng.latitude + ", Longitude: " + latLng.longitude);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_game, container, false);

        return view;
    }

    private void showGameCreateSuccessBottomSheet() {
        GameCreateSuccessBottomSheet bottomSheet = GameCreateSuccessBottomSheet.newInstance();
        bottomSheet.show(getChildFragmentManager(), "GameCreateSuccessBottomSheet");
    }
    private void showGameCreateFailBottomSheet() {
        GameCreateFailBottomSheet bottomSheet = GameCreateFailBottomSheet.newInstance();
        bottomSheet.show(getChildFragmentManager(), "GameCreateFailBottomSheet");
    }
}