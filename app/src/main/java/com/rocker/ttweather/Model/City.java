package com.rocker.ttweather.Model;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/9/7.
 * Description:
 *
 * @projectName: TTWeather
 */

public class City extends DataSupport {

    private String cityName;
    private int cityCode;
    private int provinceId;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
