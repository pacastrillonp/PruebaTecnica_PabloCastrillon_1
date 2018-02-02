package com.pruebatecnica_pablocastrillon.model;

/**
 * Created by pablo.castrillon on 2/02/2018.
 */

public class GetNotification {
    private int NotificationId;
    private String Date;
    private int Duration;


    public GetNotification() {}



    public GetNotification(int notificationId, String date, int duration) {
        NotificationId = notificationId;
        Date = date;
        Duration = duration;
    }

    public int getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(int notificationId) {
        NotificationId = notificationId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }
}
