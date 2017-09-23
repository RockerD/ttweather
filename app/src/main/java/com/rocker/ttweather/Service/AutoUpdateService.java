package com.rocker.ttweather.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import com.rocker.ttweather.App.MyApplication;
import com.rocker.ttweather.Util.HttpUtil;
import com.rocker.ttweather.Util.JsonParseUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    private static final String TAG = AutoUpdateService.class.getSimpleName();

    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBackgroundPic();
        setUpdateTime();

        return super.onStartCommand(intent, flags, startId);
    }

    private void setUpdateTime() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;  // 8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent intent = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }

    /**
     *  更新天气信息
     */
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
        String weatherStr = prefs.getString("weather", null);
        if (weatherStr != null) {
            JsonParseUtil.handleWeatherData(weatherStr);
        }
    }

    /**
     *  更新背景图片
     */
    private void updateBackgroundPic() {
        HttpUtil.sendOkHttpRequest(HttpUtil.requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "背景图片更新失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bgPicUrl = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", bgPicUrl);
                editor.apply();
            }
        });
    }
}
