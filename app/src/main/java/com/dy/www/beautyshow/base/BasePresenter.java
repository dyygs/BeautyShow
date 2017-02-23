package com.dy.www.beautyshow.base;

/**
 * Created by dy on 16/9/1.
 */
public class BasePresenter<v extends MvpView> {

    private v mvpView;

    public v getMvpView () {
        return mvpView;
    }

    public void attchView (v mvpView) {
        this.mvpView = mvpView;
    }
}
