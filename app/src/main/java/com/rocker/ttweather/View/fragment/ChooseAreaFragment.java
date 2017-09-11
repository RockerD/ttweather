package com.rocker.ttweather.View.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rocker.ttweather.Model.BaseEvent;
import com.rocker.ttweather.Presenter.FragmentPresenter;
import com.rocker.ttweather.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/8.
 * Description:
 *
 * @projectName: TTWeather
 */

public class ChooseAreaFragment extends Fragment implements ChooseAreaFragmentIView {

    private static final String TAG = ChooseAreaFragment.class.getSimpleName();

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.list_view)
    ListView listView;

    Unbinder unbinder;

    private ArrayAdapter<String> adapter;
    private ProgressDialog progressDialog;

    private FragmentPresenter fPresenter;

    @OnClick(R.id.btn_back)
    public void onBack() {
        fPresenter.goBack();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
        unbinder = ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);

        fPresenter = new FragmentPresenter(this);

        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, fPresenter.getDataList());
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fPresenter.itemClickHandle(position);
            }
        });

        fPresenter.queryProvinces();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showProgress(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
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
    public void onBaseEvent(BaseEvent event) {
        if (event.isSuccess()) {
            switch (event.getEventType()) {
                case 0:
                    fPresenter.queryProvinces();
                    break;
                case 1:
                    fPresenter.queryCities();
                    break;
                case 2:
                    fPresenter.queryCounties();
                    break;
                default:break;
            }
        }
    }


    @Override
    public void setTitleText(String titleStr) {
        titleText.setText(titleStr);
    }

    @Override
    public void setBackBtnVisibility(int visibility) {
        btnBack.setVisibility(visibility);
    }


    @Override
    public void refreshListView() {
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }
}
