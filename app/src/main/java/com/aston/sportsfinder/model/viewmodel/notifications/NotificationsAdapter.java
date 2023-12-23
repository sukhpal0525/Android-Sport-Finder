package com.aston.sportsfinder.model.viewmodel.notifications;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<Notification> notifications = new ArrayList<>();
    private final OnNotificationClickListener clickListener;
    private GameDao gameDao;
    private ExecutorService asyncTaskExecutor;

    public NotificationsAdapter(GameDao gameDao, ExecutorService asyncTaskExecutor, OnNotificationClickListener clickListener) {
        this.gameDao = gameDao;
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view, clickListener, gameDao);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification, asyncTaskExecutor);
        Log.d("SSS", "Notification: " + notification.getMessage());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setNotifications(List<Notification> newNotifications) {
        this.notifications = newNotifications;
        notifyDataSetChanged();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvGameTitle, tvLocation, tvSportType, tvDateTime;
        private Notification notification;
        private final Button btnViewDetails;
        private final GameDao gameDao;

        NotificationViewHolder(View itemView, OnNotificationClickListener listener, GameDao gameDao) {
            super(itemView);
            this.gameDao = gameDao;
            tvGameTitle = itemView.findViewById(R.id.tvGameTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSportType = itemView.findViewById(R.id.tvSportType);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onNotificationClick(notification.getGameId());
                }
            });
            btnViewDetails.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onNotificationClick(notification.getGameId());
                }
            });
        }

        void bind(Notification notification, ExecutorService asyncTaskExecutor) {
            this.notification = notification;

            asyncTaskExecutor.execute(() -> {
                Game game = gameDao.getGameById(notification.getGameId());
                if (game != null) {
                    itemView.post(() -> {
                        tvGameTitle.setText(game.getTeam1() + " vs " + game.getTeam2());
                        tvLocation.setText(game.getStreet() + ", " + game.getCity());
                        tvSportType.setText(game.getGameType());
                        tvDateTime.setText(game.getDate() + " " + game.getTime());

                        View colorIndicator = itemView.findViewById(R.id.viewGameTypeColor);
                        String gameType = game.getGameType();
                        if ("Football".equals(gameType)) { colorIndicator.setBackgroundColor(Color.parseColor("#FAD7A0"));
                        } else if ("Baseball".equals(gameType)) { colorIndicator.setBackgroundColor(Color.parseColor("#F1948A"));
                        } else if ("Rugby".equals(gameType)) { colorIndicator.setBackgroundColor(Color.parseColor("#AED6F1"));
                        } else if ("Tennis".equals(gameType)) { colorIndicator.setBackgroundColor(Color.parseColor("#83F383"));
                        } else if ("Hockey".equals(gameType)) { colorIndicator.setBackgroundColor(Color.parseColor("#D8BFD8"));
                        } else if ("Cricket".equals(gameType)) { colorIndicator.setBackgroundColor(Color.parseColor("#80CBC4"));
                        } else { colorIndicator.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        }
                    });
                }
            });
        }
    }
    public interface OnNotificationClickListener { void onNotificationClick(int gameId); }
}