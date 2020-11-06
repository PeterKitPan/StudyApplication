package com.xs.okhttp3.netutils;

import androidx.annotation.NonNull;

import java.util.Map;

/**
 * 网络工具类
 */
public class NetUtils {

    /**
     * 取消请求
     *
     * @param tag 请求标志
     */
    public static void cancleRequest(@NonNull String tag) {
        OkHttpClientManager.getInstance().cancleRequest(tag);
    }

    /**
     * 取消所有请求
     */
    public static void cancleAllRequest() {
        OkHttpClientManager.getInstance().cancleAllRequest();
    }

    /**
     * get请求 不设置请求头
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param resultCallback 请求回调
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public static void get(@NonNull String url, Map<String, String> params, @NonNull ResultCallback resultCallback, String tag) {
        OkHttpClientManager.getInstance().get(url, params, resultCallback, tag);
    }

    /**
     * get请求 自定义请求头
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public static void get(@NonNull String url, Map<String, String> params, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        OkHttpClientManager.getInstance().get(url, headers, params, resultCallback, tag);
    }

    /**
     * post请求 不设置请求头
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public static void post(@NonNull String url, @NonNull Map<String, String> params, @NonNull ResultCallback resultCallback
            , String tag) {
        OkHttpClientManager.getInstance().post(url, params, resultCallback, tag);
    }

    /**
     * post请求 自定义请求头
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public static void post(@NonNull String url, @NonNull Map<String, String> params, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        OkHttpClientManager.getInstance().post(url, params, headers, resultCallback, tag);
    }

    /**
     * post请求 （提交字符串）
     *
     * @param url            请求地址
     * @param str            提交的字符串
     * @param headers        请求头参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public static void postStr(@NonNull String url, @NonNull String str, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        OkHttpClientManager.getInstance().postStr(url, str, headers, resultCallback, tag);
    }

    /**
     * post请求 （提交Json字符串）
     *
     * @param url            请求地址
     * @param jsonStr        提交的Json字符串
     * @param headers        请求头参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public static void postJson(@NonNull String url, @NonNull String jsonStr, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        OkHttpClientManager.getInstance().postJson(url, jsonStr, headers, resultCallback, tag);
    }

    /**
     * post提交 上传文件（提交混合Form表单 文本参数+文件）
     *
     * @param url            请求地址
     * @param filePath       文件地址
     * @param formParams     提交的表单信息
     * @param headers        请求头参数
     * @param upDownCallback 请求回调
     * @param tag            请求标志
     */
    public static void uploadFile(@NonNull String url, @NonNull String filePath, Map<String, String> formParams
            , Map<String, String> headers, @NonNull UpDownCallback upDownCallback, String tag) {
        OkHttpClientManager.getInstance().uploadFile(url, filePath, formParams, headers, upDownCallback, tag);
    }

    /**
     * 下载文件（get方式）
     *
     * @param url            请求地址
     * @param filePath       文件存放路径
     * @param params         请求参数
     * @param headers        请求头参数
     * @param upDownCallback 请求回调
     * @param tag            请求标志
     */
    public static void downloadFile(@NonNull String url, @NonNull String filePath, Map<String, String> params
            , Map<String, String> headers, @NonNull UpDownCallback upDownCallback, String tag) {
        OkHttpClientManager.getInstance().downloadFile(url, filePath, params, headers, upDownCallback, tag);
    }

    /**
     * put请求（一般用来修改资源）
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public static void put(@NonNull String url, Map<String, String> params, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        OkHttpClientManager.getInstance().put(url, params, headers, resultCallback, tag);
    }

    /**
     * delete请求（一般用来删除资源）
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public static void delete(@NonNull String url, Map<String, String> params, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        OkHttpClientManager.getInstance().delete(url, params, headers, resultCallback, tag);
    }
}
