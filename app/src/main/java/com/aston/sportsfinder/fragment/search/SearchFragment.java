package com.aston.sportsfinder.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.databinding.FragmentSearchBinding;
import com.aston.sportsfinder.model.Coordinates;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

        initializeGame();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Coordinates c = game.getLocation().getCoordinates();
        LatLng loc = new LatLng(c.getLatitude(), c.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(loc)
                .title(game.getTeam1() + " vs " + game.getTeam2()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void initializeGame() {
        game = new Game();
        game.setId(1);
        game.setGameType("FOOTBALL");
        game.setTeam1("Team A");
        game.setTeam2("Team B");
        game.setDate("2021-10-12");
        game.setDateTime("10:00 AM");

        Location location = new Location();
        location.setCity("London");
        location.setCountry("UK");
        location.setState("England");

        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(51.5074);
        coordinates.setLongitude(-0.1278);
        location.setCoordinates(coordinates);

        game.setLocation(location);
    }
}
