package com.rocker.ttweather.Model.event;

import com.rocker.ttweather.Model.Weather;

/**
 * Created by Administrator on 2017/9/12.
 * Description:
 *
 * @projectName: TTWeather
 */

public class WeatherEvent{

    private boolean isSuccess;
    private Weather weather;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
