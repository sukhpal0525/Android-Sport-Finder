package com.aston.sportsfinder.fragment.game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.viewmodel.game.GameViewModel;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.ExecutorService;

public class GameCreateBottomSheet extends BottomSheetDialogFragment {


//        view.findViewById(R.id.createGameButton).setOnClickListener(v -> createGame());
//        view.findViewById(R.id.editGameButton).setOnClickListener(v -> editGame());
//        view.findViewById(R.id.deleteGameButton).setOnClickListener(v -> deleteGame());

//    private void createGame() {
//        NavHostFragment.findNavController(this).navigate(R.id.navigation_create_game);
//    }

//    private void deleteGame() {
//        asyncTaskExecutor.execute(() -> {
//            gameDao.deleteGame(game);
//        });
//    }
}