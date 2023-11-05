package com.aston.sportsfinder.fragment.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.databinding.FragmentSearchBinding;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private FragmentSearchBinding binding;
    private GoogleMap mMap;
    private Game game;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//      initializeGame();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getGamesFromDatabase();
    }

    private void getGamesFromDatabase() {
        DatabaseClient.getInstance(getContext()).executorService.execute(() -> {
            List<Game> games = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao().getAllGames();
            getActivity().runOnUiThread(() -> {
                showGamesOnMap(games);
                Log.d("ASTON", "Number of games fetched: " + games.size());
            });
        });
    }

    private void showGamesOnMap(List<Game> games) {
        for (Game game : games) {
            LatLng loc = new LatLng(game.getLatitude(), game.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title(game.getTeam1() + " vs " + game.getTeam2()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}