package com.xs.okhttp3.netutils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 应用拦截器 统一设置header，token，cookie
 */
public class HeaderInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        //从本地读取存储的cookie和token
//        String cookie = (String) SpfUtils.get(context, "cookie", "");
//        String token = (String) SpfUtils.get(context, "token", "");
//        Log.i("=====Interceptor=>", "cookie = " + cookie);
//        Log.i("=====Interceptor=>", "token = " + token);
//        builder.addHeader("token", token);
//        builder.addHeader("Cookie", cookie);
        return chain.proceed(builder.build());
    }
}
