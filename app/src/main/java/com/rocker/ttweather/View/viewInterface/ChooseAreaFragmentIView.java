package com.rocker.ttweather.View.viewInterface;

import com.rocker.ttweather.View.viewInterface.BaseIView;

/**
 * Created by Administrator on 2017/9/9.
 * Description:
 *
 * @projectName: TTWeather
 */

public interface ChooseAreaFragmentIView extends BaseIView {

    public void setTitleText(String titleStr);

    public void setBackBtnVisibility(int visibility);

    public void refreshListView();

}
