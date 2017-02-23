package com.dy.www.beautyshow.downloadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dy.www.beautyshow.R;
import com.dy.www.beautyshow.base.BaseActivity;
import com.dy.www.beautyshow.common.ActivityContext;
import com.dy.www.beautyshow.common.ImageCommonKey;
import com.dy.www.common.utils.ImageUtil;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;


public class BigImageActivity extends BaseActivity {

    private static final String filePath = "dy_relax";

    @BindView(R.id.iv_original_image)
    ImageView largeImage;

    @BindView(R.id.tv_title)
    TextView textView;

    @Inject
    @ActivityContext
    Context context;

    private String url, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        ButterKnife.bind(this);
        getComponent().inject(this);
        initData();
        Picasso.with(context).load(url).error(R.drawable.girl)
                .placeholder(R.drawable.girl).into(largeImage);
        textView.setText(title);

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(ImageCommonKey.URL)) {
            url = bundle.get(ImageCommonKey.URL).toString();
        }

        if (bundle.containsKey(ImageCommonKey.TITLE)) {
            title = bundle.get(ImageCommonKey.TITLE).toString();
        }
    }

    @OnClick(R.id.iv_original_image)
    public void onClick() {
        finish();
    }

    @OnLongClick(R.id.iv_original_image)
    public boolean onLongClick() {
        if (largeImage.getDrawable() != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) largeImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ImageUtil.saveImageToGallery(context, bitmap, filePath);
            Toast.makeText(context, "已保存到本地相册", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
