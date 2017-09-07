package com.rocker.ttweather.View.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rocker.ttweather.R;
import com.rocker.ttweather.View.BaseView;

public class MainActivity extends AppCompatActivity implements BaseView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
