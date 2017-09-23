package com.rocker.ttweather.Model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/11.
 * Description:
 *
 * @projectName: TTWeather
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{

        @SerializedName("loc")
        public String updateTime;
    }
}
