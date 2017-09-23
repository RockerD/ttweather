package com.rocker.ttweather.Model;

import com.google.gson.annotations.SerializedName;
import com.rocker.ttweather.Model.weather.AQI;
import com.rocker.ttweather.Model.weather.Basic;
import com.rocker.ttweather.Model.weather.Forecast;
import com.rocker.ttweather.Model.weather.Now;
import com.rocker.ttweather.Model.weather.Suggestion;

import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 * Description:
 *
 * @projectName: TTWeather
 */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
