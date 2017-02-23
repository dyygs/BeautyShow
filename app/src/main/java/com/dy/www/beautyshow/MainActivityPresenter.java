package com.dy.www.beautyshow;

import com.dy.www.beautyshow.base.BasePresenter;
import com.dy.www.beautyshow.remote.ImageService;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by dy on 16/9/1.
 */
public class MainActivityPresenter extends BasePresenter<MainActivityMvpView> {

    private ImageService imageService;

    private Subscription subscription;

    @Inject
    public MainActivityPresenter(ImageService imageService) {
        this.imageService = imageService;
    }
}
