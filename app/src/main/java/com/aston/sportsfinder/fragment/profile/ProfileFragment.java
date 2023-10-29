package com.aston.sportsfinder.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aston.sportsfinder.databinding.FragmentNotificationsBinding;
import com.aston.sportsfinder.databinding.FragmentProfileBinding;
import com.aston.sportsfinder.model.profile.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

//        final TextView textView = binding.welcomeMessage; profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;

    }
}
