package com.rocker.ttweather.View.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.rocker.ttweather.Model.event.BaseEvent;
import com.rocker.ttweather.Presenter.FragmentPresenter;
import com.rocker.ttweather.R;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (FragmentPresenter.isProvinceLevel()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("退出程序")
                    .setMessage("是否退出程序")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            super.onBackPressed();
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create().show();
        } else {
            BaseEvent event = new BaseEvent();
            event.setEventType(BaseEvent.EVENT_KEYBACK);
            EventBus.getDefault().post(event);
        }
    }
}
