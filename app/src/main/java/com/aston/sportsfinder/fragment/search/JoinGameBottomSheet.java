package com.aston.sportsfinder.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.model.Game;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class JoinGameBottomSheet extends BottomSheetDialogFragment {

    public static JoinGameBottomSheet newInstance() {
        return new JoinGameBottomSheet();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.join_game_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Close current bottom sheet
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> dismiss());
    }
}