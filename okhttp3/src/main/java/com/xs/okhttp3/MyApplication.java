package com.xs.okhttp3;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

public class MyApplication extends Application {
    public static boolean isDebug;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        isDebug = isDebugVersion();
    }

    /**
     * 判断是否为debug版本
     * @return true 是debug版本 false 不是的
     */
    public boolean isDebugVersion() {
        try {
            ApplicationInfo info = getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
