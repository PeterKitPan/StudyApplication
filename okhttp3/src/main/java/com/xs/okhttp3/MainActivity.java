package com.xs.okhttp3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getRequest();
        postRequest();
//        cancleRequest();
//        cancleAllRequest();

    }

    /**
     * 取消所有请求
     * @param okHttpClient
     */
    private void cancleAllRequest(OkHttpClient okHttpClient) {
        if (okHttpClient == null)
            return;
        for (Call call: okHttpClient.dispatcher().queuedCalls())
            call.cancel();
        for (Call call: okHttpClient.dispatcher().runningCalls())
            call.cancel();
    }

    /**
     * 取消相应的请求
     * @param okHttpClient
     * @param tag
     */
    private void cancleRequest(OkHttpClient okHttpClient, String tag) {
        if (okHttpClient == null || TextUtils.isEmpty(tag))
            return;
        for (Call call: okHttpClient.dispatcher().runningCalls()){
            if (tag.equals(call.request().tag())){
                call.cancel();
            }
        }
        for (Call call: okHttpClient.dispatcher().queuedCalls()){
            if (tag.equals(call.request().tag())){
                call.cancel();
            }
        }
    }

    private void postRequest() {
        File sourceFile = new File("上传文件的绝对路径");

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .readTimeout(5000,TimeUnit.SECONDS)
                .writeTimeout(5000,TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.build();

        //post提交json字符串
//        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        RequestBody requestBody = RequestBody.create(mediaType,"jsonString");

        //post提交字符串
//        MediaType mediaType = MediaType.get("text/html; charset=utf-8");
//        RequestBody requestBody = RequestBody.create(mediaType,"jsonString");

        //post提交流
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("application/octet-stream; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
//                listener.onStart();
                Source source;
                try {
                    source = Okio.source(sourceFile);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
//                        listener.onProgress(contentLength(), 1 - (float)(remaining -= readCount) / contentLength());
                    }
//                    listener.onFinish(file);
                } catch (Exception e) {
//                    listener.onError(e);
                    e.printStackTrace();
                }
            }

            @Override
            public long contentLength() throws IOException {
                return sourceFile.length();
            }
        };

        Request request = new Request.Builder()
                .addHeader("key","value")
                .url("url")
                .post(requestBody)
                .tag("tag")//可以通过该tag获取相应的请求（可以用来停止请求）
                .build();
        Call call = okHttpClient.newCall(request);
//        try {
//            call.execute();//同步
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //异步
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MSG", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200){
                    String result = response.body().string();
                    Log.e("MSG", result);
                } else {
                    Log.e("MSG", response.message());
                }
            }
        });

    }

    private void getRequest() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .readTimeout(5000,TimeUnit.SECONDS)
                .writeTimeout(5000,TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.build();
        Request request = new Request.Builder()
                .url("https://mbd.baidu.com/newspage/data/landingsuper?context=%7B%22nid%22%3A%22news_9775853084780847654%22%7D&n_type=0&p_from=1")
                .get()//get请求
                .addHeader("name","value")
                .tag("tag")//可以用来停止请求
                .build();
        Call call = okHttpClient.newCall(request);
//        try {
//            call.execute();// 同步请求
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MSG", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String result = response.body().string();
                    Log.e("MSG", result);
                }
                else {
                    Log.e("MSG", response.message());
                }
            }
        });
    }
}
