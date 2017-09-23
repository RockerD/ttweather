package com.rocker.ttweather.Util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rocker.ttweather.App.MyApplication;
import com.rocker.ttweather.Model.City;
import com.rocker.ttweather.Model.County;
import com.rocker.ttweather.Model.Province;
import com.rocker.ttweather.Model.Weather;
import com.rocker.ttweather.Model.event.WeatherEvent;
import com.rocker.ttweather.Presenter.FragmentPresenter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rocker.ttweather.Util.HttpUtil.getServiceInstance;

/**
 * Created by Administrator on 2017/9/7.
 * Description:
 *
 * @projectName: TTWeather
 */

public class JsonParseUtil {

    private static final String TAG = JsonParseUtil.class.getSimpleName();

    /**
     *   解析和处理服务器获取的省级数据
     */
    public static void handleProvinceResult(final FragmentPresenter presenter) {

        Call<String> call = getServiceInstance().getProvinceData();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Observable.just(response.body())
                        .observeOn(Schedulers.newThread())
                        .map(new Function<String, List<Province>>() {
                            @Override
                            public List<Province> apply(@NonNull String s) throws Exception {
                                List<Province> provinceList = null;

                                if (!TextUtils.isEmpty(s)) {
                                    provinceList = new ArrayList<Province>();
                                    JSONArray allProvince = new JSONArray(s);

                                    for (int i = 0; i < allProvince.length(); i++) {
                                        JSONObject provinceObject = allProvince.getJSONObject(i);

                                        Province province = new Province();
                                        province.setProvinceName(provinceObject.getString("name"));
                                        province.setProvinceCode(provinceObject.getInt("id"));

                                        provinceList.add(province);
                                    }
                                }
                                return provinceList;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Province>>() {
                            Disposable disposable;
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(@NonNull List<Province> provinces) {
                                if (provinces != null) {
                                    DataSupport.saveAll(provinces);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                disposable.dispose();
                            }

                            @Override
                            public void onComplete() {
                                presenter.queryProvinces();
                                disposable.dispose();
                            }
                        });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "获取数据失败", Toast.LENGTH_SHORT)
                        .show();
            }
        });

//        HttpUtil.getServiceInstance().getProvinceData()
//                .observeOn(Schedulers.newThread())
//                .map(new Function<String, List<Province>>() {
//                    @Override
//                    public List<Province> apply(@NonNull String s) throws Exception {
//                        Log.d(TAG, "服务器获取省级数据为：" + s);
//                        List<Province> provinceList = new ArrayList<Province>();
//
//                        if (!TextUtils.isEmpty(s)) {
//                            JSONArray allProvince = new JSONArray(s);
//                            for (int i = 0; i < allProvince.length(); i++) {
//                                JSONObject provinceObject = allProvince.getJSONObject(i);
//
//                                Province province = new Province();
//                                province.setProvinceName(provinceObject.getString("name"));
//                                province.setProvinceCode(provinceObject.getInt("id"));
//
//                                provinceList.add(province);
//                            }
//                            return provinceList;
//
//                        } else return null;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Province>>() {
//                    Disposable disposable;
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(@NonNull List<Province> provinces) {
//                        if (provinces != null) {
//                            DataSupport.saveAll(provinces);
//                            Log.d(TAG, "获取数据成功");
//                        } else Log.d(TAG, "获取数据失败");
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        disposable.dispose();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        BaseEvent event = new BaseEvent();
//                        event.setSuccess(true);
//                        event.setEventType(0);
//                        EventBus.getDefault().post(event);
//                        disposable.dispose();
//                    }
//                });
    }


    /**
     * 解析和处理服务器返回的市级数据
     */
    public static void handleCityResult(final FragmentPresenter presenter, final int provinceId) {
        Call<String> cityCall = getServiceInstance().getCityData(String.valueOf(provinceId));
        cityCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!TextUtils.isEmpty(response.body())) {
                    Observable.just(response.body())
                            .observeOn(Schedulers.newThread())
                            .map(new Function<String, List<City>>() {
                                @Override
                                public List<City> apply(@NonNull String s) throws Exception {
                                    List<City> cityList = new ArrayList<City>();
                                    JSONArray allCity = new JSONArray(s);

                                    for (int i = 0; i < allCity.length(); i++) {
                                        JSONObject cityObject = allCity.getJSONObject(i);

                                        City city = new City();
                                        city.setCityName(cityObject.getString("name"));
                                        city.setCityCode(cityObject.getInt("id"));
                                        city.setProvinceId(provinceId);

                                        cityList.add(city);
                                    }
                                    return cityList;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<City>>() {
                                Disposable disposable;
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    disposable = d;
                                }

                                @Override
                                public void onNext(@NonNull List<City> cities) {
                                    if (cities != null) {
                                        DataSupport.saveAll(cities);
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Toast.makeText(MyApplication.getContext(),
                                            "数据解析失败", Toast.LENGTH_SHORT).show();
                                    disposable.dispose();
                                }

                                @Override
                                public void onComplete() {
                                    presenter.queryCities();
                                    disposable.dispose();
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(),
                        "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * 解析和处理服务器返回的县级数据
     */
    public static void handleCountyResult(final FragmentPresenter presenter,
                                          int provinceId, final int cityId) {

        Call<String> countyCall = HttpUtil.getServiceInstance()
                .getCountyData(String.valueOf(provinceId), String.valueOf(cityId));
        countyCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!TextUtils.isEmpty(response.body())) {
                    Observable.just(response.body())
                            .observeOn(Schedulers.newThread())
                            .map(new Function<String, List<County>>() {
                                @Override
                                public List<County> apply(@NonNull String s) throws Exception {
                                    List<County> countyList = new ArrayList<County>();
                                    JSONArray allCounty = new JSONArray(s);

                                    for (int i = 0; i < allCounty.length(); i++) {
                                        JSONObject countyObject = allCounty.getJSONObject(i);

                                        County county = new County();
                                        county.setCountyName(countyObject.getString("name"));
                                        county.setWeatherId(countyObject.getString("weather_id"));
                                        county.setCityId(cityId);

                                        countyList.add(county);
                                    }
                                    return countyList;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<County>>() {
                                Disposable disposable;
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    disposable = d;
                                }

                                @Override
                                public void onNext(@NonNull List<County> counties) {
                                    if (counties != null) {
                                        DataSupport.saveAll(counties);
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                   Toast.makeText(MyApplication.getContext(),
                                           "数据解析失败", Toast.LENGTH_SHORT).show();
                                    disposable.dispose();
                                }

                                @Override
                                public void onComplete() {
                                    presenter.queryCounties();
                                    disposable.dispose();
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(),
                        "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void handleWeatherDataFromServer(String cityId, String key) {

        Call<String> call = HttpUtil.getServiceInstance().getWeatherData(cityId, key);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!TextUtils.isEmpty(response.body())) {
                    handleWeatherData(response.body());

                } else Toast.makeText(MyApplication.getContext(),
                        "网络连接失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "天气数据获取失败");
            }
        });
    }

    public static void handleWeatherData(final String response) {

        Observable.just(response)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, Weather>() {
                    @Override
                    public Weather apply(@NonNull String s) throws Exception {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
                        String weatherContent = jsonArray.getJSONObject(0).toString();
                        return new Gson().fromJson(weatherContent, Weather.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {

                        WeatherEvent event = new WeatherEvent();

                        if (weather != null && "ok".equals(weather.status)) {
                            event.setWeather(weather);
                            event.setSuccess(true);

                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(MyApplication.getContext())
                                    .edit();
                            editor.putString("weather", response);
                            editor.apply();

                        } else {
                            event.setSuccess(false);
                            Toast.makeText(MyApplication.getContext(),
                                    "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }

                        EventBus.getDefault().post(event);
                    }
                });

    }
}
