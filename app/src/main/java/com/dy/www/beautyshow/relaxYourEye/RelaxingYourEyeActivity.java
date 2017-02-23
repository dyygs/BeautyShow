package com.dy.www.beautyshow.relaxYourEye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dy.www.beautyshow.R;
import com.dy.www.beautyshow.base.BaseActivity;
import com.dy.www.beautyshow.common.ActivityContext;
import com.dy.www.beautyshow.downloadimage.DownLoadImageActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RelaxingYourEyeActivity extends BaseActivity implements RelaxingYourEyeMvpView,
        RelaxingYourEyeRecyclerViewAdapter.ItemClickListener{

    @BindView(R.id.rv_image)
    RecyclerView rvImage;

    @Inject
    @ActivityContext
    Context context;

    private String[] images;
    private RelaxingYourEyeRecyclerViewAdapter adapter;

    @Inject
    RelaxingYourEyePresenter persenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax_your_eye);
        ButterKnife.bind(this);
        getComponent().inject(this);
        persenter.attchView(this);
        initArray();
        initView();
    }

    @Override
    public void onItemClick(int position) {
        if (position >= 0 && position < images.length) {
            Intent intent = new Intent();
            intent.setClass(this, DownLoadImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("tag", images[position]);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void initArray() {
        images = new String[]{"小清新", "性感", "西洋美人", "网络美女", "诱惑", "唯美",
                "气质", "写真", "长腿", "车模", "长发"};
    }

    private void initView() {
        initRecyclerView();
    }

    private void initRecyclerView () {
        rvImage.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
//        rvImage.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        rvImage.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        adapter = new RelaxingYourEyeRecyclerViewAdapter(this, images);
        rvImage.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }
}
