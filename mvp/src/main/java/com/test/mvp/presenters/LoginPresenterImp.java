package com.test.mvp.presenters;

import android.text.TextUtils;

import com.test.mvp.models.User;
import com.test.mvp.views.LoginView;

public class LoginPresenterImp implements LoginPresenter {
    private LoginView loginView;
    @Override
    public void Login(User user) {
        if (TextUtils.isEmpty(user.getUserName()) || TextUtils.isEmpty(user.getWorkNO())){
            loginView.showToast("姓名和工号不能为空！");
            return;
        }
        if ("abc".equals(user.getUserName())&&"123".equals(user.getWorkNO())){
            loginView.showSuccess("登录成功！");
        }else {
            loginView.showError("登录失败！");
        }
    }

    @Override
    public void attachView(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void detatchView() {
        loginView = null;
    }
}
