package com.willkernel.app.practice1.entity;

import com.alibaba.fastjson.JSON;
import com.willkernel.app.practice1.net.response.Response;

/**
 * Created by willkernel on 2017/7/18.
 * mail:willkerneljc@gmail.com
 */

public class MockWeatherInfo extends MockService {
    @Override
    public String getJsonData() {
        Weather weather = new Weather();
        Weather.WeatherinfoBean info = new Weather.WeatherinfoBean();
        info.setCity("Beijing");
        info.setCityid("1000");
        weather.setWeatherinfo(info);
        Response response = getSuccessResponse();
        response.result = JSON.toJSONString(weather);
        return JSON.toJSONString(response);
    }

    private Response getSuccessResponse() {
        Response response = new Response();
        response.hasError = false;
        response.errorType = 0;
        response.errorMessage = "";
        return response;
    }
}
