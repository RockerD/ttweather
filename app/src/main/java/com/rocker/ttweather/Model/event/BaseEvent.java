package com.rocker.ttweather.Model.event;

/**
 * Created by Administrator on 2017/9/8.
 * Description:
 *
 * @projectName: TTWeather
 */

public class BaseEvent {

    public static final int EVENT_KEYBACK = 3;
//    public static final int EVENT_WEATHER = 4;

    public boolean isSuccess;
    public int eventType;
    public String Message;

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
