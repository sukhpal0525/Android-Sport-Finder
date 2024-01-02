package com.aston.sportsfinder.fragment.game;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.fragment.search.SearchViewModel;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.viewmodel.game.GameViewModel;
import com.aston.sportsfinder.model.viewmodel.notifications.NotificationsViewModel;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;

public class GameEditFragment extends Fragment {

    private GameDao gameDao;
    private ExecutorService asyncTaskExecutor;
    private Game currentGame;
    private EditText etGameType, etTeam1, etTeam2, etCapacity, etDate, etTime, etStreet, etCity, etLatitude, etLongitude, etDuration, etOrganiserName, etSkillLevel, etEquipmentNeeded, etRegistrationFee, etAdditionalNotes;
    private GameViewModel gameViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
        asyncTaskExecutor = DatabaseClient.getInstance(requireContext()).executorService;
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_game, container, false);
        gameViewModel.getSelectedGame().observe(getViewLifecycleOwner(), this::updateUIWithGameDetails);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        etGameType = view.findViewById(R.id.etGameType);
        etTeam1 = view.findViewById(R.id.etTeam1);
        etTeam2 = view.findViewById(R.id.etTeam2);
        etCapacity = view.findViewById(R.id.etCapacity);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        etStreet = view.findViewById(R.id.etStreet);
        etCity = view.findViewById(R.id.etCity);
        etLatitude = view.findViewById(R.id.etLatitude);
        etLongitude = view.findViewById(R.id.etLongitude);
        etDuration = view.findViewById(R.id.etDuration);
        etOrganiserName = view.findViewById(R.id.etOrganiserName);
        etSkillLevel = view.findViewById(R.id.etSkillLevel);
        etEquipmentNeeded = view.findViewById(R.id.etEquipmentNeeded);
        etRegistrationFee = view.findViewById(R.id.etRegistrationFee);
        etAdditionalNotes = view.findViewById(R.id.etAdditionalNotes);

        etGameType.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.getMenu().add("Football"); popupMenu.getMenu().add("Baseball"); popupMenu.getMenu().add("Rugby"); popupMenu.getMenu().add("Tennis"); popupMenu.getMenu().add("Hockey"); popupMenu.getMenu().add("Cricket");
            popupMenu.setOnMenuItemClickListener(item -> {
                etGameType.setText(item.getTitle());
                return true;
            });
            popupMenu.show();
        });

        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.navigation_search));

        Button selectLocationButton = view.findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(v -> {
            gameViewModel.setLocationSelectionMode(true);
            NavHostFragment.findNavController(this).navigate(R.id.navigation_search);
        });
        gameViewModel.getSelectedLocation().observe(getViewLifecycleOwner(), latLng -> {
            etLatitude.setText(String.valueOf(latLng.latitude));
            etLongitude.setText(String.valueOf(latLng.longitude));
            currentGame.setLatitude(latLng.latitude);
            currentGame.setLongitude(latLng.longitude);
            Log.d("SSS", "Selected location - Latitude: " + latLng.latitude + ", Longitude: " + latLng.longitude);
        });

        Button saveButton = view.findViewById(R.id.saveGame);
        saveButton.setOnClickListener(v -> {
            updateGameDetails();
        });

        etTime.setOnClickListener(v -> showTimePicker());
        etDate.setOnClickListener(v -> showDatePicker());
    }

    public void updateUIWithGameDetails(Game game) {
        if (game != null) {
            currentGame = game;
            etGameType.setText(game.getGameType() != null ? game.getGameType() : "");
            etTeam1.setText(game.getTeam1() != null ? game.getTeam1() : "");
            etTeam2.setText(game.getTeam2() != null ? game.getTeam2() : "");
            etCapacity.setText(game.getCapacity() != null ? game.getCapacity() : "0");
            etDate.setText(game.getDate() != null ? game.getDate() : "");
            etTime.setText(game.getTime() != null ? game.getTime() : "");
            etStreet.setText(game.getStreet() != null ? game.getStreet() : "");
            etCity.setText(game.getCity() != null ? game.getCity() : "");
            etLatitude.setText(game.getLatitude() != 0 ? String.valueOf(game.getLatitude()) : "");
            etLongitude.setText(game.getLongitude() != 0 ? String.valueOf(game.getLongitude()) : "");
            etDuration.setText(game.getDuration() != null ? game.getDuration() : "0");
            etOrganiserName.setText(game.getOrganiserName() != null ? game.getOrganiserName() : "");
            etSkillLevel.setText(game.getSkillLevel() != null ? game.getSkillLevel() : "");
            etEquipmentNeeded.setText(game.getEquipmentNeeded() != null ? game.getEquipmentNeeded() : "");
            etRegistrationFee.setText(game.getRegistrationFee() != null ? game.getRegistrationFee() : "0.00");
            etAdditionalNotes.setText(game.getAdditionalNotes() != null ? game.getAdditionalNotes() : "");
        }
    }

    public void updateGameDetails() {
        if (currentGame != null) {
            currentGame.setGameType(etGameType.getText().toString());
            currentGame.setTeam1(etTeam1.getText().toString());
            currentGame.setTeam2(etTeam2.getText().toString());
            currentGame.setCapacity(etCapacity.getText().toString());
            currentGame.setDate(etDate.getText().toString());
            currentGame.setTime(etTime.getText().toString());
            currentGame.setStreet(etStreet.getText().toString());
            currentGame.setCity(etCity.getText().toString());
            currentGame.setDuration(etDuration.getText().toString());
            currentGame.setOrganiserName(etOrganiserName.getText().toString());
            currentGame.setSkillLevel(etSkillLevel.getText().toString());
            currentGame.setEquipmentNeeded(etEquipmentNeeded.getText().toString());
            currentGame.setRegistrationFee(etRegistrationFee.getText().toString());
            currentGame.setAdditionalNotes(etAdditionalNotes.getText().toString());

            asyncTaskExecutor.execute(() -> {
                gameDao.updateGame(currentGame);
                getActivity().runOnUiThread(() -> {
                    Log.d("SSS", "Game was updated");
                    showGameEditSuccessBottomSheet();
                });
            });
        } else {
            Log.e("SSS", "Invalid game. Couldnt update game.");
        }
    }

    public void showTimePicker() {
        Calendar now = Calendar.getInstance();
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(now.get(Calendar.HOUR_OF_DAY)).setMinute(now.get(Calendar.MINUTE)).setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK).build();
        timePicker.addOnPositiveButtonClickListener(dialog -> {
            etTime.setText(String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute()));
        });
        timePicker.show(getParentFragmentManager(), "");
    }

    public void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            etDate.setText(String.format("%02d-%02d-%04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
        });
        datePicker.show(getParentFragmentManager(), "");
    }

    public void showGameEditSuccessBottomSheet() {
        GameEditSuccessBottomSheet bottomSheet = GameEditSuccessBottomSheet.newInstance();
        bottomSheet.show(getChildFragmentManager(), "GameEditSuccessBottomSheet");
    }
}