package com.rocker.ttweather.View.viewInterface;

import android.content.Context;

import com.rocker.ttweather.Model.Weather;

/**
 * Created by Rocker on 2017/9/23 0023.
 */

public interface WeatherIView extends BaseIView {

    public void initView();

    public void setStatusTitle();

    public void showWeatherInfo(Weather weather);

}
