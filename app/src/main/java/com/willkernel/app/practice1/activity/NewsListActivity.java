package com.willkernel.app.practice1.activity;

import android.util.Log;
import android.widget.TextView;

import com.willkernel.app.practice1.R;

public class NewsListActivity extends BActivity {

    @Override
    protected void initViews() {
        int cityId;
        String cityName;
        Log.e(TAG, "cityId=" + (cityId = getIntent().getIntExtra("cityId", 0)));
        Log.e(TAG, "cityName=" + (cityName = getIntent().getStringExtra("cityName")));
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(cityId + cityName);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_list;
    }

    @Override
    protected void setListeners() {

    }
}
