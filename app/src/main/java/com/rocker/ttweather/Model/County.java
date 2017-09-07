package com.rocker.ttweather.Model;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/9/7.
 * Description:
 *
 * @projectName: TTWeather
 */

public class County extends DataSupport {

    private String countyName;
    private String weatherId;
    private int cityId;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
