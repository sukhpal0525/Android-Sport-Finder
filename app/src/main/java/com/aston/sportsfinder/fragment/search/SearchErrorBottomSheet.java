package com.aston.sportsfinder.fragment.search;

        import android.graphics.Color;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;

        import com.aston.sportsfinder.R;
        import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SearchErrorBottomSheet extends BottomSheetDialogFragment {

    public static SearchErrorBottomSheet newInstance() {
        return new SearchErrorBottomSheet();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_error_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> dismiss());

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> dismiss());

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText("That game does not exist.");
    }
}