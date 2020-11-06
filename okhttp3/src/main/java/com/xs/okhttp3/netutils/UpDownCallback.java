package com.xs.okhttp3.netutils;

/**
 * 上传下载接口
 */
public interface UpDownCallback extends ResultCallback {
    /**
     * 请求进度(注意，上传文件时该方法在子线程中，UI操作需要转到主线程)
     *
     * @param totle 文件总大小
     * @param sum  已完成大小
     */
    void onProgress(long totle, long sum);
}
