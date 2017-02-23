package com.dy.www.beautyshow.injection.module;

import android.app.Activity;
import android.content.Context;

import com.dy.www.beautyshow.common.ActivityContext;
import com.dy.www.beautyshow.remote.ImageService;

import dagger.Module;
import dagger.Provides;


/**
 * Created by dy on 16/9/1.
 */
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule (Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity () {
        return  activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    ImageService provideImageService() {
        return ImageService.Creator.getService(activity);
    }
}
