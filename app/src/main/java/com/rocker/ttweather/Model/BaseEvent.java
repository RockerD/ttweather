package com.rocker.ttweather.Model;

/**
 * Created by Administrator on 2017/9/8.
 * Description:
 *
 * @projectName: TTWeather
 */

public class BaseEvent {

    public static final int EVENT_KEYBACK = 3;

    private boolean isSuccess;
    private int eventType;
    private String Message;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
