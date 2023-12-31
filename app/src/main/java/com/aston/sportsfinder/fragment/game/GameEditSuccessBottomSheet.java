package com.aston.sportsfinder.fragment.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.aston.sportsfinder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class GameEditSuccessBottomSheet extends BottomSheetDialogFragment {

    public static GameEditSuccessBottomSheet newInstance() {
        return new GameEditSuccessBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_game_edit_success, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> dismiss());

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.navigation_search));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}