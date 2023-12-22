package com.aston.sportsfinder.fragment.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.databinding.FragmentSearchBinding;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private FragmentSearchBinding binding;
    private GoogleMap mMap;
    private ExecutorService asyncTaskExecutor;
    private String searchQuery;
    private EditText searchBar;

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
        searchBar = view.findViewById(R.id.searchBar);

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchForGame(searchBar.getText().toString());
                return true;
            }
            return false;
        });

//        binding.buttonFootball.setOnClickListener(v -> searchForGame("Football"));
//        binding.buttonBaseball.setOnClickListener(v -> searchForGame("Baseball"));
//        binding.buttonRugby.setOnClickListener(v -> searchForGame("Rugby"));

        view.findViewById(R.id.buttonAll).setOnClickListener(v -> searchForGame(""));
        view.findViewById(R.id.buttonFootball).setOnClickListener(v -> filterGamesByType("Football"));
        view.findViewById(R.id.buttonBaseball).setOnClickListener(v -> filterGamesByType("Baseball"));
        view.findViewById(R.id.buttonRugby).setOnClickListener(v -> filterGamesByType("Rugby"));
        view.findViewById(R.id.buttonTennis).setOnClickListener(v -> filterGamesByType("Tennis"));
        view.findViewById(R.id.buttonHockey).setOnClickListener(v -> filterGamesByType("Hockey"));

        if (getArguments() != null) {
            searchQuery = getArguments().getString("query", "");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(marker -> {
            Game game = (Game) marker.getTag();
            if (game != null) {
                showGameDetailsBottomSheet(game);
            }
            return false;
        });
        getGamesFromDatabase();
    }

    public void showGameDetailsBottomSheet(Game game) {
        GameDetailsBottomSheet bottomSheet = GameDetailsBottomSheet.newInstance(game);
        bottomSheet.show(getChildFragmentManager(), "GameDetailsBottomSheet");
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

    public void showErrorBottomSheet() {
        SearchErrorBottomSheet bottomSheet = SearchErrorBottomSheet.newInstance();
        bottomSheet.show(getChildFragmentManager(), "SearchErrorBottomSheet");
    }

    public void showGamesOnMap(List<Game> games) {
        mMap.clear();
        for (Game game : games) {
            Log.d("SSS", games.size() + " games loaded");
            LatLng loc = new LatLng(game.getLatitude(), game.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(loc)
                    .title(game.getTeam1() + " vs " + game.getTeam2());

            if (game.isJoined()) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(game);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
        }
    }
}