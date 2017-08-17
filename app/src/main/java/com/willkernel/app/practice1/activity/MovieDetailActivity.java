package com.willkernel.app.practice1.activity;

import android.util.Log;
import android.webkit.WebView;

import com.willkernel.app.practice1.R;
import com.willkernel.app.practice1.entity.MovieInfo;
import com.willkernel.app.wklib.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends BActivity {

    private WebView wvAds;

    @Override
    protected void initViews() {
        wvAds = (WebView) findViewById(R.id.wvAds);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void setListeners() {
        String data_template = FileUtil.getFromAssets(this, "data1_template.html");
        StringBuilder content = new StringBuilder();

        List<MovieInfo> movieInfoList = setMovieList();
        for (MovieInfo movieInfo : movieInfoList) {
            String rowData = data_template.replace("<name/>", movieInfo.name).replace("<price/>", movieInfo.price);
            content.append(rowData);
        }
        String htmlData = FileUtil.getFromAssets(this, "102.html");
        htmlData = htmlData.replace("<data1/>", content);
        wvAds.loadData(htmlData, "text/html", "utf-8");
    }

    private List<MovieInfo> setMovieList() {
        int movieId=getIntent().getIntExtra("movieId",0);
        String movieName=getIntent().getStringExtra("movieName");
        Log.e(TAG,"movieId="+movieId);
        Log.e(TAG,"movieName="+movieName);
        ArrayList<MovieInfo> movieList = new ArrayList<>();
        movieList.add(new MovieInfo("Movie 1", "120"));
        movieList.add(new MovieInfo("Movie B", "80"));
        movieList.add(new MovieInfo("Movie III", "60"));
        return movieList;
    }
}
