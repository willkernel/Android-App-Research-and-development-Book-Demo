package com.willkernel.app.practice1.mockdata;

import com.alibaba.fastjson.JSON;
import com.willkernel.app.practice1.entity.UserInfo;
import com.willkernel.app.practice1.net.response.Response;

/**
 * Created by willkernel on 2017/7/27.
 * mail:willkerneljc@gmail.com
 */

public class MockLoginSuccessInfo extends MockService {
    @Override
    public String getJsonData() {
        UserInfo userInfo = new UserInfo();
        userInfo.loginName = "Login";
        userInfo.userName = "User";
        userInfo.score = 100;
        Response response = getSuccessResponse();
        response.result = JSON.toJSONString(userInfo);
        return JSON.toJSONString(response);
    }
}