package com.xs.okhttp3.netutils;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.xs.okhttp3.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * 1.HEAD 向服务器索要与GET请求相一致的响应，只不过响应体将不会被返回。这一方法可以在不必传输整个响应内容的情况下，就可以获取包含在响应消息头中的元信息。
 * 2.GET 向特定的资源发出请求。注意：GET方法不应当被用于产生“副作用”的操作中，例如在web app.中。其中一个原因是GET可能会被网络蜘蛛等随意访问。
 * 3.POST 向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST请求可能会导致新的资源的建立或已有资源的修改。
 * 4.PUT 向指定资源位置上传其最新内容——修改。
 * 5.DELETE 请求服务器删除Request-URI所标识的资源。
 * 6.TRACE 回显服务器收到的请求，主要用于测试或诊断。
 * 7.CONNECT HTTP/1.1协议中预留给能够将连接改为管道方式的代理服务器。
 * 8.OPTIONS 返回服务器针对特定资源所支持的HTTP请求方法。也可以利用向Web服务器发送'*'的请求来测试服务器的功能性。
 */
public class OkHttpClientManager {

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mainHandler;
    private HttpLoggingInterceptor loggingInterceptor;//okhttp日志拦截器log输出的默认TAG是“==》okhttp”

    private OkHttpClientManager() {
        File cache = Environment.getExternalStorageDirectory().getAbsoluteFile();
        int cacheSize = 10 * 1024 * 1024;//设置缓存大小
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//连接超时(单位:秒)
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时(单位:秒)
                .readTimeout(20, TimeUnit.SECONDS)//读取超时(单位:秒)
                .pingInterval(20, TimeUnit.SECONDS) //websocket轮训间隔(单位:秒)
                .cache(new Cache(cache, cacheSize))//设置缓存
                .followRedirects(true)//运行请求重定向
                .sslSocketFactory(initSSLSocketFactory(), (X509TrustManager) initTrustManager()[0])
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        if("自己的url".equals(hostname)){
                            return true;
                        }
                        else {
                            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                            return hv.verify(hostname, session);
                        }
                    }
                })// https支持
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        //保存刷新cookie
                        cookieStore.remove(url);
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                });//设置cookie支持

        if (MyApplication.isDebug) {
            builder.addNetworkInterceptor(loggingInterceptor);//添加日志拦截器应（网络拦截器（发起和接收））
        }
        //看情况添加
        builder.addInterceptor(new HeaderInterceptor());//添加请求头拦截器（应用拦截器（发起请求之前））

        mOkHttpClient = builder.build();
        mainHandler = new Handler(Looper.getMainLooper());
//        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }



    /**
     * 取消请求
     *
     * @param tag 请求标志
     */
    public void cancleRequest(@NonNull String tag) {
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求
     */
    public void cancleAllRequest() {
        mOkHttpClient.dispatcher().cancelAll();
    }

    /**
     * get异步请求
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public void get(@NonNull String url, Map<String, String> params, @NonNull ResultCallback resultCallback, String tag) {
        get(url, null, params, resultCallback, tag);
    }

    /**
     * get异步请求
     *
     * @param url            请求地址
     * @param headers        请求头参数
     * @param params         请求参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public void get(@NonNull String url, Map<String, String> headers, Map<String, String> params
            , @NonNull ResultCallback resultCallback, String tag) {
        sendStart(resultCallback);
        Request request = RequestManager.createGetRequest(url, params, headers, tag);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailure(e, resultCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    sendSuccess(response.headers(), response.body().string(), response.code(), resultCallback);
                } else {
                    sendError(response.headers(), response.body().string(), response.code(), resultCallback);
                }
            }
        });
    }

    /**
     * post请求 不设置请求头 （提交Form表单）
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public void post(@NonNull String url, @NonNull Map<String, String> params, ResultCallback resultCallback
            , String tag) {
        post(url, params, null, resultCallback, tag);
    }

    /**
     * post请求 自定义请求头 （提交Form表单）
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头参数
     * @param resultCallback 请求回调
     * @param tag            请求标志
     */
    public void post(@NonNull String url, @NonNull Map<String, String> params, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        sendStart(resultCallback);
        Request request = RequestManager.createPostRequest(url, headers, params, tag);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailure(e, resultCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    sendSuccess(response.headers(), response.body().string(), response.code(), resultCallback);
                } else {
                    sendError(response.headers(), response.body().string(), response.code(), resultCallback);
                }
            }
        });
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
    public void postStr(@NonNull String url, @NonNull String str, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        sendStart(resultCallback);
        Request request = RequestManager.createPostRequest(url, str, headers, tag, 0);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailure(e, resultCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    sendSuccess(response.headers(), response.body().string(), response.code(), resultCallback);
                } else {
                    sendError(response.headers(), response.body().string(), response.code(), resultCallback);
                }
            }
        });
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
    public void postJson(@NonNull String url, @NonNull String jsonStr, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        sendStart(resultCallback);
        Request request = RequestManager.createPostRequest(url, jsonStr, headers, tag, 1);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailure(e, resultCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    sendSuccess(response.headers(), response.body().string(), response.code(), resultCallback);
                } else {
                    sendError(response.headers(), response.body().string(), response.code(), resultCallback);
                }
            }
        });
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
    public void uploadFile(@NonNull String url, @NonNull String filePath, Map<String, String> formParams
            , Map<String, String> headers, @NonNull UpDownCallback upDownCallback, String tag) {
        sendStart(upDownCallback);
        Request request = RequestManager.createPostRequest(url, filePath, formParams, headers, upDownCallback, tag);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailure(e, upDownCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    sendSuccess(response.headers(), response.body().string(), response.code(), upDownCallback);
                } else {
                    sendError(response.headers(), response.body().string(), response.code(), upDownCallback);
                }
            }
        });
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
    public void downloadFile(@NonNull String url, @NonNull String filePath, Map<String, String> params
            , Map<String, String> headers, @NonNull UpDownCallback upDownCallback, String tag) {
        sendStart(upDownCallback);
        Request request = RequestManager.createGetRequest(url, params, headers, tag);

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailure(e, upDownCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        final long total = response.body().contentLength();
                        long sum = 0L;
                        File file = new File(filePath);
                        File dir = file.getParentFile();
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        fos = new FileOutputStream(file);
                        int len = 0;
                        while ((len = is.read(buf)) != -1) {
                            sum += (long) len;
                            fos.write(buf, 0, len);
                            sendProgress(total, sum * 1L, upDownCallback);
                        }
                        fos.flush();
                        sendSuccess(response.headers(), response.body().string(), response.code(), upDownCallback);
                    } catch (Exception e) {
                        sendFailure(e, upDownCallback);
                    } finally {
                        try {
                            response.body().close();
                            if (is != null) {
                                is.close();
                            }
                        } catch (IOException e) {
                        }

                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                } else {
                    sendError(response.headers(), response.body().string(), response.code(), upDownCallback);
                }
            }
        });
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
    public void put(@NonNull String url, Map<String, String> params, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        sendStart(resultCallback);
        Request request = RequestManager.createPutRequest(url, params, headers, tag);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailure(e, resultCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    sendSuccess(response.headers(), response.body().string(), response.code(), resultCallback);
                } else {
                    sendError(response.headers(), response.body().string(), response.code(), resultCallback);
                }
            }
        });
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
    public void delete(@NonNull String url, Map<String, String> params, Map<String, String> headers
            , @NonNull ResultCallback resultCallback, String tag) {
        sendStart(resultCallback);
        Request request = RequestManager.createDeleteRequest(url, params, headers, tag);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailure(e, resultCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    sendSuccess(response.headers(), response.body().string(), response.code(), resultCallback);
                } else {
                    sendError(response.headers(), response.body().string(), response.code(), resultCallback);
                }
            }
        });
    }

    /**
     * 线程调度——开始
     *
     * @param resultCallback 主线程回调
     */
    private void sendStart(ResultCallback resultCallback) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                resultCallback.startRequest();
            }
        });
    }

    /**
     * 线程调度——失败
     *
     * @param e              错误
     * @param resultCallback 主线程回调
     */
    private void sendFailure(Exception e, ResultCallback resultCallback) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                resultCallback.onFailure(e);
            }
        });
    }

    /**
     * 线程调度——成功
     *
     * @param headers        返回的报文头
     * @param response       返回信息
     * @param code           返回状态码
     * @param resultCallback 主线程回调
     */
    private void sendSuccess(Headers headers, String response, int code, ResultCallback resultCallback) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                resultCallback.onSuccess(headers, response, code);
            }
        });
    }

    /**
     * 线程调度——接口返回有错误
     *
     * @param headers        返回的报文头
     * @param response       返回信息
     * @param code           返回状态码
     * @param resultCallback 主线程回调
     */
    private void sendError(Headers headers, String response, int code, ResultCallback resultCallback) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                resultCallback.onError(headers, response, code);
            }
        });
    }

    /**
     * 线程调度——下载进度
     *
     * @param total          总大小
     * @param sum            已下载大小
     * @param upDownCallback 回调
     */
    private void sendProgress(long total, long sum, UpDownCallback upDownCallback) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                upDownCallback.onProgress(total, sum);
            }
        });
    }

    public static SSLSocketFactory initSSLSocketFactory() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null,
                    initTrustManager(), new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }

    public static TrustManager[] initTrustManager() {
        TrustManagerFactory trustManagerFactory = null;

        try {
            //创建一个X.509格式的CertificateFactory
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            //证书工厂根据证书文件的流生成证书Certificate
            InputStream is = new Buffer()
                    .writeUtf8("证书字符串")
                    .inputStream();
            Certificate ca = certificateFactory.generateCertificate(is);

            //创建KeyStore，用来存储信任证书
            KeyStore keyStor = createEmptyKeyStore();
            keyStor.setCertificateEntry("ca",ca);

            //创建一个默认类型的TrustManagerFactory
            String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);

            //用keyStore实例初始化TrustManagerFactory,此时TrustManagerFactory会信任我们保存的证书
            trustManagerFactory.init(keyStor);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        //通过trustManagerFactory获取TrustManager数组，TrustManager也会信任keyStor中的证书
        return trustManagerFactory.getTrustManagers();
    }

    /**
     * 创建一个空白的KeyStore
     * @return KeyStore
     * @throws GeneralSecurityException
     */
    private static KeyStore createEmptyKeyStore() throws GeneralSecurityException {
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            //此处密码随意,不会产生问题
            keystore.load(null, "password".toCharArray());
            return keystore;
        } catch (IOException e) {
            return null;
        }
    }
}
