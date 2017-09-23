package com.rocker.ttweather.Util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    public static final String BASE_URL = "http://guolin.tech";
    public static final String WEATHER_KEY_CODE = "bc0418b57b2d4918819d3974ac1285d9";

    //每日一图地址
    public static final String requestBingPic = "http://guolin.tech/api/bing_pic";

    private static HttpService service;

    public interface HttpService {

        @GET("/api/china")
        Call<String> getProvinceData();

        @GET("/api/china/{city_id}")
        Call<String> getCityData(@Path("city_id") String city_id);

        @GET("/api/china/{city_id}/{county_id}")
        Call<String> getCountyData(@Path("city_id") String city_id,
                                               @Path("county_id") String county_id);

        @GET("/api/weather")
        Call<String> getWeatherData(@Query("cityid") String cityid, @Query("key") String key);

    }

    public static HttpService getServiceInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        if (service == null) {
            service = retrofit.create(HttpService.class);
        }

        return service;
    }

    public static void sendOkHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
