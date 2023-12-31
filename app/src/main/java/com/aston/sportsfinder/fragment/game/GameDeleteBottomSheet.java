package com.aston.sportsfinder.fragment.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.viewmodel.game.GameViewModel;
import com.aston.sportsfinder.util.DatabaseClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class GameDeleteBottomSheet extends BottomSheetDialogFragment {

    private Game game;
    private GameDao gameDao;
    private ExecutorService asyncTaskExecutor;
    private GameViewModel gameViewModel;


    public static GameDeleteBottomSheet newInstance(Game game) {
        GameDeleteBottomSheet fragment = new GameDeleteBottomSheet();
        fragment.game = game;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
        asyncTaskExecutor = DatabaseClient.getInstance(requireContext()).executorService;
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_game_delete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> dismiss());

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            deleteGame();
            dismiss();
        });

        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dismiss());
    }

    public void deleteGame() {
        asyncTaskExecutor.execute(() -> {
            gameDao.deleteGame(game);
            List<Game> updatedGames = gameDao.getAllGames();
            getActivity().runOnUiThread(() -> {
                gameViewModel.updateGames(updatedGames);
                dismiss();
            });
        });
    }
}