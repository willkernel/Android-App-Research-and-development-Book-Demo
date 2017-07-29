package com.willkernel.app.wklib.mock;


import com.willkernel.app.wklib.net.response.Response;

/**
 * Created by willkernel on 2017/7/18.
 * mail:willkerneljc@gmail.com
 */

public abstract class MockService {
   public abstract String getJsonData();

   protected Response getSuccessResponse() {
      Response response = new Response();
      response.hasError = false;
      response.errorType = 0;
      response.errorMessage = "";
      return response;
   }
}