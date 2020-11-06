package com.xs.retrofit;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

public interface ApiService {

    String baseUrl = "https://api.github.com/";

    @GET("users")
    Call<Response> get(@QueryMap Map<String,String> params);

    @GET("users")
    Call<Response> get(@QueryMap Map<String,String> params,@HeaderMap Map<String, String> headers);



//    @GET("users/{user}/repos")
//    Call<List<User>> listRepos(@Path("user") String user);
//
//    @GET("users/login/{user}")
//    Call<T> login(@Path("user") String user);
//
//    @GET("users/{check}")
//    Call<User> check(@Path("check") String check,@Query("user") String user);
//
//    /**
//     * 统一的header在拦截器OkHttp interceptor里设置
//     */
//    @Headers({
//            "Accept: application/vnd.github.v3.full+json",
//            "User-Agent: Retrofit-Sample-App"
//    })
//    @GET("users/{check}")
//    Call<User> check(@Path("check") String check,@QueryMap Map<String,String> users);
//
//    @GET("user")
//    Call<User> getUser(@Header("Authorization") String authorization);
//
//    @GET("user")
//    Call<User> getUser(@HeaderMap Map<String, String> headers);
//
//    @POST("users/new")
//    Call<User> createUser(@Body User user);
//
//    @FormUrlEncoded
//    @POST("user/edit")
//    Call<User> updateUser(@Field("first_name") String first, @Field("last_name") String last);
//
//    @Multipart
//    @PUT("user/photo")
//    Call<User> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);
}
