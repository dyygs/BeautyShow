package com.dy.www.beautyshow.base;

import android.support.v7.app.AppCompatActivity;

import com.dy.www.beautyshow.injection.inject.ActivityComponent;
import com.dy.www.beautyshow.injection.inject.DaggerActivityComponent;
import com.dy.www.beautyshow.injection.module.ActivityModule;


/**
 * Created by dy on 16/9/1.
 */
public class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    public ActivityComponent getComponent () {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .build();
        }

        return  activityComponent;
    }
}
