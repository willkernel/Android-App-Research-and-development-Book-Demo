package com.willkernel.app.practice1.mockdata;


import com.alibaba.fastjson.JSON;
import com.willkernel.app.practice1.entity.Weather;
import com.willkernel.app.wklib.mock.MockService;
import com.willkernel.app.wklib.net.response.Response;

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
}
