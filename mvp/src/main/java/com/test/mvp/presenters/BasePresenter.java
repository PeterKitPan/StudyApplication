package com.test.mvp.presenters;

public interface BasePresenter<T> {
    void attachView(T t);
    void detatchView();
}
