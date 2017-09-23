package com.rocker.ttweather.Model.weather;

/**
 * Created by Administrator on 2017/9/11.
 * Description:
 *
 * @projectName: TTWeather
 */

public class AQI {

    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }

}
