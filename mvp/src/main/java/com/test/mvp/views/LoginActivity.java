package com.test.mvp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.test.mvp.R;
import com.test.mvp.models.User;
import com.test.mvp.presenters.LoginPresenterImp;

public class LoginActivity extends AppCompatActivity implements LoginView{
    private LoginPresenterImp loginPresenterImp;
    private Button bt_login;
    private EditText et_name, et_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenterImp = new LoginPresenterImp();
        loginPresenterImp.attachView(this);
        bt_login = findViewById(R.id.bt_login);
        et_name = findViewById(R.id.et_name);
        et_no = findViewById(R.id.et_no);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(et_name.getText().toString(), et_no.getText().toString());
                loginPresenterImp.Login(user);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenterImp.detatchView();
    }

    @Override
    public void showError(String errors) {
        Toast.makeText(this,errors,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccess(String success) {
        Toast.makeText(this,success,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
