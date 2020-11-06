package com.xs.okhttp3.netutils;

import okhttp3.Headers;

/**
 * 返回接口
 */
public interface ResultCallback {

    /**
     * 开始请求
     */
    void startRequest();

    /**
     * 请求失败
     *
     * @param e 异常
     */
    void onFailure(Exception e);

    /**
     * 请求成功
     *
     * @param headers  返回报文头
     * @param response 返回信息
     * @param code     返回状态码
     */
    void onSuccess(Headers headers, String response, int code);

    /**
     * 接口返回失败
     *
     * @param headers  返回报文头
     * @param response 返回信息
     * @param code     返回状态码
     */
    void onError(Headers headers, String response, int code);

}
