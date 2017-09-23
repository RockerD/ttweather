package com.rocker.ttweather.View.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rocker.ttweather.App.MyApplication;
import com.rocker.ttweather.Model.Weather;
import com.rocker.ttweather.Model.event.BaseEvent;
import com.rocker.ttweather.Model.event.WeatherEvent;
import com.rocker.ttweather.Model.weather.Forecast;
import com.rocker.ttweather.Presenter.WeatherPresenter;
import com.rocker.ttweather.R;
import com.rocker.ttweather.Service.AutoUpdateService;
import com.rocker.ttweather.Util.JsonParseUtil;
import com.rocker.ttweather.View.viewInterface.WeatherIView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity implements WeatherIView {

    private static final String TAG = WeatherActivity.class.getSimpleName();

    @BindView(R.id.title_city)
    TextView titleCity;

    @BindView(R.id.title_update_time)
    TextView titleUpdateTime;

    @BindView(R.id.degree_text)
    TextView degreeText;

    @BindView(R.id.weather_info_text)
    TextView weatherInfoText;

    @BindView(R.id.forecast_layout)
    LinearLayout forecastLayout;

    @BindView(R.id.aqi_text)
    TextView aqiText;

    @BindView(R.id.pm25_text)
    TextView pm25Text;

    @BindView(R.id.comfort_text)
    TextView comfortText;

    @BindView(R.id.car_wash_text)
    TextView carWashText;

    @BindView(R.id.sport_text)
    TextView sportText;

    @BindView(R.id.weather_layout)
    ScrollView weatherLayout;

    @BindView(R.id.bing_pic_img)
    ImageView bingPicImg;

    @BindView(R.id.swipeRefresh)
    public SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;

    @BindView(R.id.nav_btn)
    Button navBtn;

    @OnClick(R.id.nav_btn)
    public void selectLocation() {
        //展开DrawerLayout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private WeatherPresenter weatherPresenter;
    private ProgressDialog progressDialog;
    private String mWeatherId;

    public static void startWeatherActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusTitle();
        setContentView(R.layout.activity_weather);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initView();
    }

    @Override
    public void initView() {

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);  //获取本地天气信息
        String weatherBgImg = prefs.getString("bing_pic", null);  //获取本地背景图片信息

        if (weatherBgImg == null) {
            weatherPresenter = new WeatherPresenter();
            weatherPresenter.loadBingPic();
        } else {
            Glide.with(this).load(weatherBgImg).into(bingPicImg);
        }

        if (weatherString != null) {
            JsonParseUtil.handleWeatherData(weatherString);

        } else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            requestWeather(mWeatherId);
        }
    }

    //不知为何状态栏和界面没有融合
    @Override
    public void setStatusTitle() {
        if (Build.VERSION.SDK_INT >= 21) {
            Log.d(TAG, "StatusTitle is setting!");
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void requestWeather(String weatherId) {

        JsonParseUtil.handleWeatherDataFromServer(weatherId, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!TextUtils.isEmpty(response.body())) {
                    JsonParseUtil.handleWeatherData(response.body());

                } else Toast.makeText(MyApplication.getContext(),
                        "网络连接失败！", Toast.LENGTH_SHORT).show();

                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.d(TAG, "天气数据获取失败");
            }
        });
    }

    @Override
    public void showWeatherInfo(Weather weather) {

        if (weather != null) {
            //启动后台服务
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);

            //显示天气信息
            String cityName = weather.basic.cityName;
            String updateTime = weather.basic.update.updateTime.split(" ")[1];
            String degree = weather.now.temperature + "℃";

            String weatherInfo = weather.now.more.info;
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);

            forecastLayout.removeAllViews();
            for (Forecast forecast : weather.forecastList) {
                View view = LayoutInflater.from(this)
                        .inflate(R.layout.weather_forcast_item, forecastLayout, false);

                TextView dateText = (TextView) view.findViewById(R.id.date_text);
                TextView infoText = (TextView) view.findViewById(R.id.info_text);
                TextView maxText = (TextView) view.findViewById(R.id.max_text);
                TextView minText = (TextView) view.findViewById(R.id.min_text);

                dateText.setText(forecast.date);
                infoText.setText(forecast.more.info);
                maxText.setText(forecast.temperature.max);
                minText.setText(forecast.temperature.min);

                forecastLayout.addView(view);
            }

            if (weather.aqi != null) {
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }

            String comfort = "舒适度：" + weather.suggestion.comfort.info;
            String carWash = "洗车指数：" + weather.suggestion.carWash.info;
            String sport = "运动建议：" + weather.suggestion.sport.info;
            comfortText.setText(comfort);
            carWashText.setText(carWash);
            sportText.setText(sport);

        } else {
            Toast.makeText(this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void showProgress(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void closeProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherEvent(WeatherEvent event) {
        if (event.isSuccess()) {
            Weather weather = event.getWeather();
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
//            swipeRefresh.setRefreshing(false);

        } else Toast.makeText(this, "获取天启信息失败！", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseEvent(BaseEvent event) {
        if (event.isSuccess()) {
            Glide.with(this).load(event.getMessage()).into(bingPicImg);

        } else Toast.makeText(this, "获取天启信息失败！", Toast.LENGTH_SHORT).show();
    }

}
