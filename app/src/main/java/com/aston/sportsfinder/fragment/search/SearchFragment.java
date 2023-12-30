package com.aston.sportsfinder.fragment.search;

import static com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.databinding.FragmentSearchBinding;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.viewmodel.game.GameViewModel;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;

import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private FragmentSearchBinding binding;
    private GoogleMap mMap;
    private ExecutorService asyncTaskExecutor;
    private String searchQuery;
    private EditText searchBar;
    private GameViewModel gameViewModel;
    private List<Game> latestGames;
    private boolean isMapInitialized = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private ImageButton createGameButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        asyncTaskExecutor = DatabaseClient.getInstance(getContext()).executorService;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        Log.d("SSS", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        RadioGroup sportsRadioGroup = view.findViewById(R.id.sportsRadioGroup);
        RadioButton buttonAll = view.findViewById(R.id.buttonAll);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        gameViewModel.getGamesLiveData().observe(getViewLifecycleOwner(), games -> {
            latestGames = games;
            if (mMap != null) {
                // Show games only if map is ready
                showGamesOnMap(games);
            }
        });

        createGameButton = binding.btnCreateGame;
        createGameButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.navigation_create_game);
        });

        searchBar = view.findViewById(R.id.searchBar);
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchForGame(searchBar.getText().toString());
                return true;
            }
            return false;
        });

        buttonAll.setChecked(true);
        sportsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.buttonAll) {
                searchForGame("");
            } else if (checkedId == R.id.buttonFootball) {
                filterGamesByType("Football");
            } else if (checkedId == R.id.buttonBaseball) {
                filterGamesByType("Baseball");
            } else if (checkedId == R.id.buttonRugby) {
                filterGamesByType("Rugby");
            } else if (checkedId == R.id.buttonTennis) {
                filterGamesByType("Tennis");
            } else if (checkedId == R.id.buttonHockey) {
                filterGamesByType("Hockey");
            }
        });
//        binding.buttonFootball.setOnClickListener(v -> searchForGame("Football"));

//        view.findViewById(R.id.buttonAll).setOnClickListener(v -> searchForGame(""));
//        view.findViewById(R.id.buttonFootball).setOnClickListener(v -> filterGamesByType("Football"));

        if (getArguments() != null) {
            searchQuery = getArguments().getString("query", "");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        createLocationRequest();
        setupLocationCallback();
        ToggleButton toggleLocation = view.findViewById(R.id.toggle_location);
        toggleLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                } else {
                    startLocationUpdates();
                }
            } else {
                stopLocationUpdates();
            }
        });
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (latestGames != null) {
            showGamesOnMap(latestGames);
        }
        mMap.setOnMarkerClickListener(marker -> {
            Game game = (Game) marker.getTag();
            if (game != null) {
                showGameDetailsBottomSheet(game);
            }
            return false;
        });
        getGamesFromDatabase();
        mMap.setOnMapClickListener(latLng -> {
            Log.d("SSS", "LatLng: " + latLng.latitude + ", " + latLng.longitude);
            if (gameViewModel.isLocationSelectionMode()) {
                gameViewModel.setSelectedLocation(latLng);
                gameViewModel.setLocationSelectionMode(false);
                NavHostFragment.findNavController(this).popBackStack();
            }
        });
    }

    public void getGamesFromDatabase() {
        Log.d("SSS", "Fetching games from database for query: " + searchQuery);
        asyncTaskExecutor.execute(() -> {
            List<Game> games = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao().searchGames(searchQuery);
            getActivity().runOnUiThread(() -> {
                showGamesOnMap(games);
            });
        });
    }

    public void searchForGame(String query) {
        asyncTaskExecutor.execute(() -> {
            List<Game> games = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao().searchGames(query);
            getActivity().runOnUiThread(() -> {
                if (games.isEmpty()) {
                    showErrorBottomSheet();
                } else {
                    showGamesOnMap(games);
                }
            });
        });
    }

    public void filterGamesByType(String gameType) {
        asyncTaskExecutor.execute(() -> {
            List<Game> games = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao().getGamesByType(gameType);
            getActivity().runOnUiThread(() -> {
                if (games.isEmpty()) {
                    showErrorBottomSheet();
                } else {
                    showGamesOnMap(games);
                }
            });
        });
    }

    public void showGamesOnMap(List<Game> games) {
        mMap.clear();
        for (Game game : games) {
            Log.d("SSS", games.size() + " games loaded");
            LatLng loc = new LatLng(game.getLatitude(), game.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(loc)
                    .title(game.getTeam1() + " vs " + game.getTeam2());

            if (game.isCreatedByUser() && game.isJoined()) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else if (game.isCreatedByUser()) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            } else if (game.isJoined()) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(game);

            if (!isMapInitialized) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 6));
                isMapInitialized = true;
            }
        }
    }

    private final ActivityResultLauncher<String> locationPermissionRequest = registerForActivityResult
            (new ActivityResultContracts.RequestPermission(), isGranted -> {
                ToggleButton toggleLocation = getView().findViewById(R.id.toggle_location);
                if (isGranted) {
                    startLocationUpdates();
                } else {
                    // Ask again unless precise
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showPermissionErrorBottomSheet();
                        toggleLocation.setChecked(false);
                    } else {
                        // permanently disable permission requesting
                        showPermissionDeniedBottomSheet();
                        toggleLocation.setChecked(false);
                    }
                }
            });

    protected void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(10000)
                .setMinUpdateIntervalMillis(2000)
                .setPriority(PRIORITY_HIGH_ACCURACY)
                .build();
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d("SSS", "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude());
                }
            }
        };
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
        }
    }

    public void showErrorBottomSheet() {
        SearchErrorBottomSheet bottomSheet = SearchErrorBottomSheet.newInstance();
        bottomSheet.show(getChildFragmentManager(), "SearchErrorBottomSheet");
    }

    public void showGameDetailsBottomSheet(Game game) {
        GameDetailsBottomSheet bottomSheet = GameDetailsBottomSheet.newInstance(game);
        bottomSheet.show(getChildFragmentManager(), "GameDetailsBottomSheet");
    }

    public void showPermissionErrorBottomSheet() {
        PermissionErrorBottomSheet bottomSheet = new PermissionErrorBottomSheet();
        bottomSheet.show(getChildFragmentManager(), "PermissionErrorBottomSheet");
    }

    public void showPermissionDeniedBottomSheet() {
        PermissionDeniedBottomSheet bottomSheet = new PermissionDeniedBottomSheet();
        bottomSheet.show(getChildFragmentManager(), "PermissionDeniedBottomSheet");
    }
}