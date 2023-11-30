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

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
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
        View bottomDivider;

        NotificationViewHolder(View itemView) {
            super(itemView);
            textViewNotification = itemView.findViewById(R.id.textViewNotification);
            bottomDivider = itemView.findViewById(R.id.bottomDivider);
        }

        void bind(Notification notification) {
            textViewNotification.setText(notification.getMessage());
        }
    }
}