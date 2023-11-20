package com.aston.sportsfinder.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.aston.sportsfinder.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        NavController navController = Navigation.findNavController(view);
//        view.findViewById(R.id.searchButton).setOnClickListener(v -> {
//            NavOptions navOptions = new NavOptions.Builder()
//                    .setPopUpTo(R.id.navigation_home, true)
//                    .build();
//            navController.navigate(R.id.action_navigation_home_to_search, null, navOptions);
//        });
    }
}









//            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
//            NavigationUtil.deselectBottomNavItems(bottomNavigationView);
            //            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            //            bottomNavigationView.setSelectedItemId(R.id.navigation_search);



//  new test
//        view.findViewById(R.id.searchButton).setOnClickListener(v -> {
//            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_search);
//            BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
//            bottomNav.setSelectedItemId(R.id.navigation_search); // Update the selected item to searchFragment
//        });


//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.welcomeMessage;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }
//
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.navigation_search);
//            }
////
////                BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
////                bottomNav.setSelectedItemId(R.id.navigation_search);
////                // HOME Fragment ----> SEARCH Fragment
////                NavHostFragment.findNavController(HomeFragment.this)
////                        .navigate(R.id.action_HomeFragment_to_SearchFragment);
//        });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}