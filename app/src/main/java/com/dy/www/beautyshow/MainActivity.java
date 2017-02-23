package com.dy.www.beautyshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.dy.www.beautyshow.Service.DownloadMVService;
import com.dy.www.beautyshow.base.BaseActivity;
import com.dy.www.beautyshow.common.ActivityContext;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements MainActivityMvpView {

    @Inject
    MainActivityPresenter presenter;

    @Inject
    @ActivityContext
    Context context;

    @BindView(R.id.ll_main)
    LinearLayout llMain;

    @BindView(R.id.btn_relax_your_eye)
    Button buttonRelax;

    @BindView(R.id.btn_open_your_eye)
    Button buttonOpen;

    @BindView(R.id.vv_mv)
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attchView(this);
        setOnClickListener();
    }

    private void setOnClickListener () {
        buttonRelax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.setClass(context, RelaxingYourEyeActivity.class);
                intent.setAction("action");
                startActivity(intent);
            }
        });

        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, DownloadMVService.class);
                startService(intent);
//                Uri uri = Uri.parse("http://dd.yinyuetai.com/uploads/videos/common/44960139807B7DBCA60D66156BB7C8DE.mp4");
//                videoView.setMediaController(new MediaController(context));
//                videoView.setVideoURI(uri);
//                videoView.requestFocus();
//                videoView.start();
            }
        });
    }
}
