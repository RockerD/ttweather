package com.rocker.ttweather.Model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/11.
 * Description:
 *
 * @projectName: TTWeather
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }
}
