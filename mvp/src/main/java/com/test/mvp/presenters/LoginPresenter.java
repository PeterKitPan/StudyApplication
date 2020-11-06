package com.test.mvp.presenters;

import com.test.mvp.models.User;
import com.test.mvp.views.LoginView;

public interface LoginPresenter extends BasePresenter<LoginView> {
    void Login(User user);
}
