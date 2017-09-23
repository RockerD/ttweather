package com.rocker.ttweather.Presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.rocker.ttweather.App.MyApplication;
import com.rocker.ttweather.Model.City;
import com.rocker.ttweather.Model.County;
import com.rocker.ttweather.Model.Province;
import com.rocker.ttweather.Model.event.BaseEvent;
import com.rocker.ttweather.Util.JsonParseUtil;
import com.rocker.ttweather.View.activity.WeatherActivity;
import com.rocker.ttweather.View.fragment.ChooseAreaFragment;
import com.rocker.ttweather.View.viewInterface.ChooseAreaFragmentIView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/9.
 * Description:
 *
 * @projectName: TTWeather
 */

public class FragmentPresenter implements BaseIPresenter {

    private static final String TAG = FragmentPresenter.class.getSimpleName();

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ChooseAreaFragmentIView view;
    private Context context;

    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;  //省级列表
    private List<City> cityList;          //市级列表
    private List<County> countyList;      //县级列表

    private Province currentProvince;     //当前省份
    private City currentCity;             //当前城市
    private static int currentLevel;             //选中的级别

    public FragmentPresenter(ChooseAreaFragmentIView view, Context context) {
        this.view = view;
        this.context = context;
        EventBus.getDefault().register(this);
    }

    public void goBack() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        }
    }

    public void itemClickHandle(int position) {
        if (currentLevel == LEVEL_PROVINCE) {
            currentProvince = provinceList.get(position);
            queryCities();

        } else if (currentLevel == LEVEL_CITY) {
            currentCity = cityList.get(position);
            queryCounties();

        } else if (currentLevel == LEVEL_COUNTY) {
            String weatherId = countyList.get(position).getWeatherId();
            Intent intent = new Intent(context, WeatherActivity.class);
            intent.putExtra("weather_id", weatherId);

            WeatherActivity.startWeatherActivity(context, intent);
            ((ChooseAreaFragment)view).getActivity().finish();
        }
    }

    public List<String> getDataList() {
        return dataList;
    }

    /**
     * 查询所有省份，优先从数据库查询，没有再到服务器查询
     */
    public void queryProvinces() {
        view.setTitleText("中国");
        view.setBackBtnVisibility(View.GONE);
        view.showProgress(MyApplication.getContext());

        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            view.refreshListView();
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(LEVEL_PROVINCE);
        }
        view.closeProgress();
    }

    /**
     * 查询选中省份所有城市，优先从数据库查询，没有再到服务器查询
     */
    public void queryCities() {
        view.setTitleText(currentProvince.getProvinceName());
        view.setBackBtnVisibility(View.VISIBLE);

        cityList = DataSupport.where("provinceId = ?",
                String.valueOf(currentProvince.getProvinceCode()))
                .find(City.class);

        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            view.refreshListView();
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(LEVEL_CITY);
        }
    }

    /**
     * 查询选中城市所有的县，优先从数据库查询，没有再到服务器查询
     */
    public void queryCounties() {
        view.setTitleText(currentCity.getCityName());
        view.setBackBtnVisibility(View.VISIBLE);

        countyList = DataSupport.where("cityId = ?", String.valueOf(currentCity.getCityCode()))
                .find(County.class);

        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            view.refreshListView();
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(LEVEL_COUNTY);
        }
    }

    /**
     * 根据传入地址和类型从服务器查询所有省市县数据
     */
    private void queryFromServer(int type) {
        switch (type) {
            case LEVEL_PROVINCE:
                JsonParseUtil.handleProvinceResult(FragmentPresenter.this);
                break;
            case LEVEL_CITY:
                JsonParseUtil.handleCityResult(FragmentPresenter.this,
                        currentProvince.getProvinceCode());
                break;
            case LEVEL_COUNTY:
                JsonParseUtil.handleCountyResult(FragmentPresenter.this,
                        currentProvince.getProvinceCode(), currentCity.getCityCode());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackEvent(BaseEvent event) {
        if (event.getEventType() == BaseEvent.EVENT_KEYBACK) {
            goBack();
        }
    }

    public static boolean isProvinceLevel() {
        if (currentLevel == LEVEL_PROVINCE)
            return true;
        else return false;
    }
}
