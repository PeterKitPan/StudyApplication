package com.xs.permission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button bt_story;
    private static final int STORAGE = 0;
    private static final int FLAG_REQUEST_PERMISSION = 1000;
    private static final int FLAG_SETTINGS_PERMISSION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_story = findViewById(R.id.bt_story);
        bt_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkHasPermission()) {
                    Log.e("MSG", "用户有权限");
                } else {
                    Log.e("MSG", "用户没有权限");
                }
            }
        });
    }

    private boolean checkHasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int storagePermision = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (storagePermision == PackageManager.PERMISSION_GRANTED) {
                //有权限
                return true;
            } else {
                //没有权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (STORAGE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户授予了权限
                Log.e("MSG", "用户授予了权限");
            } else {
                //用户没有授权
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //只点击了拒绝
                    Log.e("MSG", "点击了拒绝");
                    showPermissionDialog(FLAG_REQUEST_PERMISSION);
                } else {
                    //点击了不再提示
                    Log.e("MSG", "点击了不再提示");
                    showPermissionDialog(FLAG_SETTINGS_PERMISSION);
                }
            }
        }
    }


    private void showPermissionDialog(final int flag) {
        new AlertDialog.Builder(this).setTitle("授权提示")
                .setMessage("需要授予存储权限才能使用该功能。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户取消了授权，整个路程结束，不执行任何操作。
                        Toast.makeText(MainActivity.this, "不使用此功能", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (flag) {
                            case FLAG_REQUEST_PERMISSION:
                                // 用户之前没有点击“不再提示”，但是此处选在继续授权的分支
                                // 请求系统授权对话框
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE);
                                break;
                            case FLAG_SETTINGS_PERMISSION:
                                // 用户之前点击过“不再提示”，此处选择继续授权的分支
                                // 到APP的详情页手动授权。
                                Toast.makeText(MainActivity.this, "请到设置中心开启相关权限", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }).create().show();
    }
}
