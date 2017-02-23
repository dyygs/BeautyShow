package com.dy.www.beautyshow.downloadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dy.www.beautyshow.R;
import com.dy.www.beautyshow.common.ImageCommonKey;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by dy on 16/9/2.
 */
public class NormalRecyclerViewAdapter extends RecyclerView.Adapter {


    private Context context;
    private ArrayList list = new ArrayList();
    private LayoutInflater inflater;
    private int x;
    private int y;
    private Bitmap oldBitmap;

    NormalRecyclerViewAdapter(Context context, ArrayList list, int x, int y) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.x = x;
        this.y = y;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_image, parent,  false);
        return new NormalImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Map<String, Object> map = (Map)list.get(position);
        final String url = map.get(ImageCommonKey.SOURCEURL).toString();
        final String title = map.get(ImageCommonKey.TITLE).toString();
        if (holder instanceof NormalImageViewHolder) {
            CropSquareTransformation transformation = new CropSquareTransformation();
            Picasso.with(context)
                    .load(url)
                    .transform(transformation)
                    .error(R.drawable.girl)
                    .placeholder(R.drawable.girl)
                    .into(((NormalImageViewHolder) holder).ivImage);
            ((NormalImageViewHolder) holder).ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(url, title);
                    }
                }
            });
            //((NormalImageViewHolder) holder).ivImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //((NormalImageViewHolder) holder).tvImageTitle.setText(title);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NormalImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvImageTitle;
        NormalImageViewHolder(View view) {
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.iv_image);
            tvImageTitle = (TextView) view.findViewById(R.id.tv_image_title);
        }
    }


    public class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int tmp = x/2;
            float height;
            Bitmap result;
            Matrix matrix = new Matrix();
            height =((float)tmp/(float)source.getWidth());
            matrix.postScale(height,height); //长和宽放大缩小的比例
            if (source.getWidth() < tmp) {
                result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight());
            } else {

                result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            }

            if (result != source) {
                source.recycle();
            }
            return result;
        }
        @Override
        public String key() { return "square()"; }
    }
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onClick(String url, String title);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
