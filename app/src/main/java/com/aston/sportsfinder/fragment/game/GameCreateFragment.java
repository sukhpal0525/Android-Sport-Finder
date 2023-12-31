package com.aston.sportsfinder.fragment.game;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.TimeZone;
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
        EditText etStreet = view.findViewById(R.id.etStreet);
        EditText etCity = view.findViewById(R.id.etCity);
        EditText etLatitude = view.findViewById(R.id.etLatitude);
        EditText etLongitude = view.findViewById(R.id.etLongitude);
        EditText etDuration = view.findViewById(R.id.etDuration);
        EditText etOrganiserName = view.findViewById(R.id.etOrganiserName);
        EditText etSkillLevel = view.findViewById(R.id.etSkillLevel);
        EditText etEquipmentNeeded = view.findViewById(R.id.etEquipmentNeeded);
        EditText etRegistrationFee = view.findViewById(R.id.etRegistrationFee);
        EditText etAdditionalNotes = view.findViewById(R.id.etAdditionalNotes);

        etGameType.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.getMenu().add("Football"); popupMenu.getMenu().add("Baseball"); popupMenu.getMenu().add("Rugby"); popupMenu.getMenu().add("Tennis"); popupMenu.getMenu().add("Hockey"); popupMenu.getMenu().add("Cricket");
            popupMenu.setOnMenuItemClickListener(item -> {
                etGameType.setText(item.getTitle());
                return true;
            });
            popupMenu.show();
        });

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
            String street = etStreet.getText().toString();
            String city = etCity.getText().toString();


            latitude = Double.parseDouble(etLatitude.getText().toString());
            longitude = Double.parseDouble(etLongitude.getText().toString());


            String duration = etDuration.getText().toString();
            String organiserName = etOrganiserName.getText().toString();
            String skillLevel = etSkillLevel.getText().toString();

            String equipmentNeeded = etEquipmentNeeded.getText().toString();
            String registrationFee = etRegistrationFee.getText().toString();
            String additionalNotes = etAdditionalNotes.getText().toString();


            Game newGame = new Game(gameType, team1, team2, 0, 0, false, false, street, city, "", "", latitude, longitude, date, time, organiserName, capacity, 0, skillLevel, equipmentNeeded, duration, registrationFee, additionalNotes, true);
            asyncTaskExecutor.execute(() -> {
                gameViewModel.setNewGame(newGame);
                gameDao.insertGame(newGame);
                getActivity().runOnUiThread(() -> {
                    showGameCreateSuccessBottomSheet();
                });
            });
        });

        etTime.setOnClickListener(v -> showTimePicker());
        etDate.setOnClickListener(v -> showDatePicker());

        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.navigation_search));

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

    public void showTimePicker() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)).setMinute(Calendar.getInstance().get(Calendar.MINUTE)).setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK).build();
        timePicker.addOnPositiveButtonClickListener(dialog -> {
            String formattedTime = String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute());
            EditText etTime = getView().findViewById(R.id.etTime);
            etTime.setText(formattedTime);
        });
        timePicker.show(getParentFragmentManager(), "TimePicker");
    }

    public void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            String formattedDate = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            EditText etDate = getView().findViewById(R.id.etDate);
            etDate.setText(formattedDate);
        });
        datePicker.show(getParentFragmentManager(), "DatePicker");
    }

    public void showGameCreateSuccessBottomSheet() {
        GameCreateSuccessBottomSheet bottomSheet = GameCreateSuccessBottomSheet.newInstance();
        bottomSheet.show(getChildFragmentManager(), "GameCreateSuccessBottomSheet");
    }
    public void showGameCreateFailBottomSheet() {
        GameCreateFailBottomSheet bottomSheet = GameCreateFailBottomSheet.newInstance();
        bottomSheet.show(getChildFragmentManager(), "GameCreateFailBottomSheet");
    }
}