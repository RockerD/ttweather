package com.rocker.ttweather.Presenter;

import android.widget.Toast;

import com.rocker.ttweather.App.MyApplication;
import com.rocker.ttweather.Model.event.BaseEvent;
import com.rocker.ttweather.Util.HttpUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Rocker on 2017/9/22 0022.
 */

public class WeatherPresenter implements BaseIPresenter {

    public void loadBingPic() {
        HttpUtil.sendOkHttpRequest(HttpUtil.requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MyApplication.getContext(),
                        "图片加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPicAddress = response.body().string();

                BaseEvent event = new BaseEvent();
                event.setSuccess(true);
                event.setMessage(bingPicAddress);

                EventBus.getDefault().post(event);
            }
        });
    }
}
