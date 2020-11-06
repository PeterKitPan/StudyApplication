package com.xs.retrofit;

import android.os.Environment;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class RetrofitService {
    private static RetrofitService retrofitService;
    private Retrofit retrofit;


    private RetrofitService() {
        File cache = Environment.getExternalStorageDirectory().getAbsoluteFile();
        int cacheSize = 10 * 1024 * 1024;//设置缓存大小
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//连接超时(单位:秒)
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时(单位:秒)
                .readTimeout(20, TimeUnit.SECONDS)//读取超时(单位:秒)
                .pingInterval(20, TimeUnit.SECONDS) //websocket轮训间隔(单位:秒)
                .cache(new Cache(cache, cacheSize))//设置缓存
                .followRedirects(true)//运行请求重定向
//                .sslSocketFactory(initSSLSocketFactory(), initTrustManager())
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                })// https支持
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.baseUrl)
                .client(mOkHttpClient)
//                .addConverterFactory(G)
                .build();
    }

    public static RetrofitService getInstance(){
        if (retrofitService == null){
            synchronized (RetrofitService.class){
                if (retrofitService == null){
                    retrofitService = new RetrofitService();
                }
            }
        }
        return retrofitService;
    }

    public void get(Map<String,String> headers, Map<String,String> params, Callback callback){
        retrofit.create(ApiService.class).get(params,headers).enqueue(callback);
    }
}
