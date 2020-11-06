package com.xs.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private void retrofit() {
        Map<String, String> params = new HashMap<>();
        RetrofitService.getInstance().get(null, params, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    /**
     * 判断是否为debug版本
     *
     * @param context
     * @return
     */
    public static boolean isDebugVersion(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
