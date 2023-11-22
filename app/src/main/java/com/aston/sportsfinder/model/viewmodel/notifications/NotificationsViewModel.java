package com.aston.sportsfinder.model.viewmodel.notifications;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.aston.sportsfinder.dao.NotificationDao;
import com.aston.sportsfinder.dao.UserDao;
import com.aston.sportsfinder.model.Notification;
import com.aston.sportsfinder.util.DatabaseClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class NotificationsViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<Notification>> notificationsLiveData = new MediatorLiveData<>();
    private final MutableLiveData<Integer> unreadNotificationsCount = new MutableLiveData<>();
    private final NotificationDao notificationDao;
    private final UserDao userDao;
    private final ExecutorService executorService;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        notificationDao = DatabaseClient.getInstance(application).getAppDatabase().notificationDao();
        userDao = DatabaseClient.getInstance(application).getAppDatabase().userDao();
        executorService = DatabaseClient.getInstance(application).executorService;

        loadNotifications();
    }

    public void refreshNotifications() {
        loadNotifications();
    }

    public void loadNotifications() {
        executorService.execute(() -> {
            Integer userId = userDao.getCurrentUserId();
            if (userId != null) {
                List<Notification> notifications = notificationDao.getNotificationsForUserSync(userId);
                notificationsLiveData.postValue(notifications);
                updateUnreadNotificationsCount(userId);
            } else {
                notificationsLiveData.postValue(new ArrayList<>());
            }
        });
    }

    public void markNotificationsAsRead() {
        executorService.execute(() -> {
            Integer userId = userDao.getCurrentUserId();
            if (userId != null) {
                notificationDao.markNotificationsAsRead(userId);
                int notificationCount = notificationDao.getUnreadNotificationCount(userId);
                unreadNotificationsCount.postValue(notificationCount);
            }
        });
    }

    private void updateUnreadNotificationsCount(int userId) {
        int notificationCount = notificationDao.getUnreadNotificationCount(userId);
        unreadNotificationsCount.postValue(notificationCount);
    }

    public LiveData<List<Notification>> getNotifications() {
        return notificationsLiveData;
    }

    public LiveData<Integer> getUnreadNotificationsCount() {
        return unreadNotificationsCount;
    }
}