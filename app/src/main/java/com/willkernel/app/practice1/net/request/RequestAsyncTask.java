package com.willkernel.app.practice1.net.request;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.willkernel.app.practice1.net.response.Response;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 * <p>
 * 详解HttpURLConnection http://blog.csdn.net/woxueliuyun/article/details/43267365
 * 设置连接参数的方法
 * setAllowUserInteraction
 * setDoInput
 * setDoOutput
 * setIfModifiedSince
 * setUseCaches
 * setDefaultAllowUserInteraction
 * setDefaultUseCaches
 * 设置请求头或响应头
 * HTTP请求允许一个key带多个用逗号分开的values，但是HttpURLConnection只提供了单个操作的方法：
 * <p>
 * setRequestProperty(key,value)
 * addRequestProperty(key,value)
 * setRequestProperty和addRequestProperty的区别就是，
 * setRequestProperty会覆盖已经存在的key的所有values，有清零重新赋值的作用。
 * 而addRequestProperty则是在原来key的基础上继续添加其他value。
 * <p>
 * 发送URL请求
 * 建立实际连接之后，就是发送请求，把请求参数传到服务器，这就需要使用outputStream把请求参数传给服务器：
 * <p>
 * getOutputStream
 * 获取响应
 * 请求发送成功之后，即可获取响应的状态码，如果成功既可以读取响应中的数据，获取这些数据的方法包括：
 * <p>
 * getContent
 * getHeaderField
 * getInputStream
 * 对于大部分请求来说，getInputStream和getContent是用的最多的。
 * <p>
 * 相应的信息头用以下方法获取：
 * <p>
 * getContentEncoding
 * getContentLength
 * getContentType
 * getDate
 * getExpiration
 * getLastModifed
 */

public abstract class RequestAsyncTask extends AsyncTask<String, Void, Response> {
    protected abstract void onSuccess(String content);

    protected abstract void onFail(String errorMessage);

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Response doInBackground(String... params) {
        return getResponse(params[0]);
    }

    private Response getResponse(String param) {
        Response response = new Response();
        try {
            URL url = new URL(param);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法为"POST"，默认是GET
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(20000);
            httpURLConnection.setReadTimeout(20000);
//            System.setProperty("sun.NET.client.defaultConnectTimeout", "30000");
//            System.setProperty("sun.net.client.defaultReadTimeout", "30000");

            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            // 4.0中设置httpCon.setDoOutput(true),将导致请求以post方式提交,即使设置了httpCon.setRequestMethod("GET");
            // 将代码中的httpCon.setDoOutput(true);删除即可
            // httpURLConnection.setDoOutput(true);  // GET 请求设置输出时，抛出 FileNotFoundException: http://www.weather.com.cn/data/sk/101010100.html
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            //POST 不使用缓存
            httpURLConnection.setUseCaches(false);

            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");

            //连接 配置必须要在connect之前完成
            httpURLConnection.connect();

            // 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
            // 所以在开发中不调用上述的connect()也可以)。
//            OutputStream outStrm = httpURLConnection.getOutputStream();

            // HttpURLConnection发送请求
            // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
//            ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);

            // 向对象输出流写出数据，这些数据将存到内存缓冲区中
//            objOutputStrm.writeObject(new String(""));

            // 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
//            objOutputStrm.flush();

            // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
            // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
//            objOutputStrm.close();

            // 调用HttpURLConnection连接对象的getInputStream()函数,
            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
            InputStream inStrm = httpURLConnection.getInputStream(); // <===注意，实际发送请求的代码段就在这里

            // 上边的httpConn.getInputStream()方法已调用,本次HTTP请求已结束,下边向对象输出流的输出已无意义，
            // 既使对象输出流没有调用close()方法，下边的操作也不会向对象输出流写入任何数据.
            // 因此，要重新发送数据时需要重新创建连接、重新设参数、重新创建流对象、重新写数据、
            // 重新发送数据(至于是否不用重新这些操作需要再研究)
            //objOutputStrm.writeObject(new String(""));
            //httpURLConnection.getInputStream();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStrm.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            response.result = baos.toString("UTF-8");
            if (TextUtils.isEmpty(response.result)) {
                response.errorMessage = "error";
                response.errorType = -1;
                response.hasError = true;
            } else {
                response.errorMessage = "";
                response.errorType = 0;
                response.hasError = false;
            }
        } catch (Exception e) {
            response.errorMessage = "error";
            response.errorType = -1;
            response.hasError = true;
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(Response response) {
        if (response.hasError) {
            onFail(response.errorMessage);
        } else {
            onSuccess(response.result);
        }
    }
}