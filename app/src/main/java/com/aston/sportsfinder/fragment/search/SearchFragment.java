package com.aston.sportsfinder.fragment.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
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

import java.util.List;
import java.util.concurrent.ExecutorService;

public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private FragmentSearchBinding binding;
    private GoogleMap mMap;
    private ExecutorService asyncTaskExecutor;
    private String searchQuery;

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

    public void showGamesOnMap(List<Game> games) {
        for (Game game : games) {
            LatLng loc = new LatLng(game.getLatitude(), game.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(loc)
                    .title(game.getTeam1() + " vs " + game.getTeam2());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));

            if (game.isJoined()) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            mMap.addMarker(markerOptions).setTag(game);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}