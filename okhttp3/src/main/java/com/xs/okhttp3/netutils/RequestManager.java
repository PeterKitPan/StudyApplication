package com.xs.okhttp3.netutils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * OkHttp Request管理类
 */
public class RequestManager {

    /**
     * 生成get请求的Request
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头参数
     * @param tag     请求标志 （用来取消、停止请求）
     * @return 返回Request
     */
    public static Request createGetRequest(@NonNull String url, Map<String, String> params
            , Map<String, String> headers, String tag) {
        StringBuilder stringBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=")
                        .append(entry.getValue()).append("&");
            }
        }
        Request.Builder builder = new Request.Builder().get().
                url(stringBuilder.substring(0, stringBuilder.length() - 1));
        if (headers != null && headers.size() > 0) {
            builder.header("User-Agent", "OkHttp3 Headers.java & Kotlin");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }
        return builder.build();
    }

    /**
     * 生成post请求的Request
     *
     * @param url     请求地址
     * @param headers 请求头参数
     * @param params  请求参数
     * @param tag     请求标志 （用来取消、停止请求）
     * @return 返回Request
     */
    public static Request createPostRequest(@NonNull String url, Map<String, String> headers
            , @NonNull Map<String, String> params, String tag) {
        Request.Builder builder = new Request.Builder().url(url);
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody formBody = formBodyBuilder.build();
        if (headers != null && headers.size() > 0) {
            builder.header("User-Agent", "OkHttp3 Headers.java & Kotlin");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }
        builder.post(formBody);
        return builder.build();
    }

    /**
     * 生成post提交字符串请求的Request
     *
     * @param url      请求地址
     * @param str      提交内容
     * @param headers  请求头参数
     * @param tag      请求标志 （用来取消、停止请求）
     * @param typeCode MediaTypele类型标志 （0是普通字符串， 1是Json字符串）
     * @return 返回Request
     */
    public static Request createPostRequest(@NonNull String url, @NonNull String str, Map<String
            , String> headers, String tag, int typeCode) {
        MediaType mediaType = MediaType.parse("text/html; charset=utf-8");
        Request.Builder builder = new Request.Builder().url(url);
        if (typeCode == 1)
            mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, str);
        if (headers != null && headers.size() > 0) {
            builder.header("User-Agent", "OkHttp3 Headers.java & Kotlin");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }
        builder.post(requestBody);
        return builder.build();
    }

    /**
     * post上传文件请求的Request
     *
     * @param url        请求地址
     * @param filePath   上传文件路径
     * @param formParams 请求参数
     * @param headers    请求头参数
     * @param tag        请求标志 （用来取消、停止请求）
     * @return 返回Request
     */
    public static Request createPostRequest(@NonNull String url, @NonNull String filePath, Map<String, String> formParams
            , Map<String, String> headers, UpDownCallback upDownCallback, String tag) {
        Request.Builder builder = new Request.Builder().url(url);
        File sourceFile = new File(filePath);
        RequestBody fileRequestBody = new RequestBody() {

            @Override
            public MediaType contentType() {
                return MediaType.parse("application/octet-stream; charset=utf-8");
            }

            @Override
            public long contentLength() throws IOException {
                return sourceFile.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(sourceFile);
                    Buffer buf = new Buffer();
                    Long finished = 0L;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        upDownCallback.onProgress(contentLength(), finished += readCount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .addPart(fileRequestBody);
        if (headers != null && headers.size() > 0) {
            builder.header("User-Agent", "OkHttp3 Headers.java & Kotlin");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (formParams != null && formParams.size() > 0) {
            for (Map.Entry<String, String> entry : formParams.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = requestBodyBuilder.addPart(formBodyBuilder.build()).build();
        builder.post(requestBody);
        if (!TextUtils.isEmpty(tag))
            builder.tag(tag);
        return builder.build();
    }

    /**
     * put请求的Request
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头参数
     * @param tag     请求标志 （用来取消、停止请求）
     * @return 返回Request
     */
    public static Request createPutRequest(@NonNull String url, Map<String, String> params
            , Map<String, String> headers, String tag) {
        Request.Builder builder = new Request.Builder().url(url);
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody formBody = formBodyBuilder.build();
        if (headers != null && headers.size() > 0) {
            builder.header("User-Agent", "OkHttp3 Headers.java & Kotlin");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }
        builder.put(formBody);
        return builder.build();
    }

    /**
     * delete请求的Request
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头参数
     * @param tag     请求标志 （用来取消、停止请求）
     * @return 返回Request
     */
    public static Request createDeleteRequest(@NonNull String url, Map<String, String> params
            , Map<String, String> headers, String tag) {
        Request.Builder builder = new Request.Builder().url(url);
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody formBody = formBodyBuilder.build();
        if (headers != null && headers.size() > 0) {
            builder.header("User-Agent", "OkHttp3 Headers.java & Kotlin");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }
        builder.delete(formBody);
        return builder.build();
    }
}
