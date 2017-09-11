package com.rocker.ttweather.Util;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/9/7.
 * Description:
 *
 * @projectName: TTWeather
 */

public class HttpUtil {

    private static final String ProvinceUrl = "http://guolin.tech/api/china";
    private static final String CityUrl = "http://guolin.tech/api/china/city_di";
    private static final String CountyUrl = "http://guolin.tech/api/china/city_id/county_id";

    private static HttpService service;

    public interface HttpService {

        @GET("/api/china")
        Call<String> getProvinceData();

        @GET("/api/china/{city_id}")
        Call<String> getCityData(@Path("city_id") String city_id);

        @GET("/api/china/{city_id}/{county_id}")
        Call<String> getCountyData(@Path("city_id") String city_id,
                                               @Path("county_id") String county_id);

    }

    public static HttpService getServiceInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://guolin.tech")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        if (service == null) {
            service = retrofit.create(HttpService.class);
        }

        return service;
    }

}
