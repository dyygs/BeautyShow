package com.dy.www.beautyshow.injection.inject;

import com.dy.www.beautyshow.MainActivity;
import com.dy.www.beautyshow.downloadimage.BigImageActivity;
import com.dy.www.beautyshow.downloadimage.DownLoadImageActivity;
import com.dy.www.beautyshow.injection.module.ActivityModule;
import com.dy.www.beautyshow.relaxYourEye.RelaxingYourEyeActivity;

import dagger.Component;


/**
 * Created by dy on 16/9/1.
 */
@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(DownLoadImageActivity downLoadImageActivity);
    void inject(RelaxingYourEyeActivity relaxingYourEyeActivity);
    void inject(BigImageActivity bigImageActivity);
}
