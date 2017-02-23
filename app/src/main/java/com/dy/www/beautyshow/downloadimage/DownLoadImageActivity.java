package com.dy.www.beautyshow.downloadimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;

import com.dy.www.beautyshow.R;
import com.dy.www.beautyshow.base.BaseActivity;
import com.dy.www.beautyshow.common.ActivityContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;



public class DownLoadImageActivity extends BaseActivity implements DownLoadImageMvpView{

    ArrayList list  = new ArrayList();

    @Inject
    DownLoadImagePresenter downLoadImagePresenter;

    @Inject
    @ActivityContext
    Context context;
    @BindView(R.id.rv_image_list)
    RecyclerView rvImageList;

    private String tag;
    private NormalRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_image);
        ButterKnife.bind(this);
        getComponent().inject(this);
        downLoadImagePresenter.attchView(this);
        ButterKnife.bind(this);
        prepareData();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (list.size() == 0) {
            downLoadImagePresenter.getNews(tag);
        }
    }

    @Override
    public void setImages(List<Map<String, Object>> imageList) {
        if (imageList != null && imageList.size() > 0) {
            list.clear();
            list.addAll(imageList);
            adapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerView () {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int x = size.x;
        final int y = size.y;
//        rvImageList.setLayoutManager(new LinearLayoutManager(this));
//        rvImageList.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
        rvImageList.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        adapter = new NormalRecyclerViewAdapter(this, list, x, y);
        adapter.setOnItemClickListener(new NormalRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(String url, String title) {
                Intent intent = new Intent();
                intent.setClass(DownLoadImageActivity.this, BigImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("title", title);
                intent.putExtras(bundle);
//                intent.setAction(Intent.ACTION_PICK);
//                intent.setDataAndType(Uri.parse("file:///storage/sdcard0/dy_relax/1.jpg"), "image/jpg");
//                startActivity(intent);

            }
        });
        rvImageList.setAdapter(adapter);
    }

    private void prepareData() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("tag")) {
            tag = bundle.getString("tag");
        }
    }
}
