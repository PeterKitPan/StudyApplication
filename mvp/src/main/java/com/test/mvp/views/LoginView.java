package com.test.mvp.views;

public interface LoginView extends BaseView {
    void showError(String errors);
    void showSuccess(String success);
}
