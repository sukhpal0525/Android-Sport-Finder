package com.aston.sportsfinder.model.viewmodel.notifications;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aston.sportsfinder.R;
import com.aston.sportsfinder.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<Notification> notifications = new ArrayList<>();
    private final OnNotificationClickListener clickListener;

    public NotificationsAdapter(OnNotificationClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);
        Log.d("SSS", "Notification: " + notification.getMessage());

        if (position == notifications.size() - 1) {
            holder.bottomDivider.setVisibility(View.VISIBLE);
        } else {
            holder.bottomDivider.setVisibility(View.GONE);
        }
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

        private final TextView textViewNotification;
        private Notification notification;
        View bottomDivider;

        NotificationViewHolder(View itemView, OnNotificationClickListener listener) {
            super(itemView);
            textViewNotification = itemView.findViewById(R.id.textViewNotification);
            bottomDivider = itemView.findViewById(R.id.bottomDivider);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onNotificationClick(notification.getGameId());
                }
            });
        }

        void bind(Notification notification) {
            this.notification = notification;
            textViewNotification.setText(notification.getMessage());
        }
    }
    public interface OnNotificationClickListener { void onNotificationClick(int gameId); }
}