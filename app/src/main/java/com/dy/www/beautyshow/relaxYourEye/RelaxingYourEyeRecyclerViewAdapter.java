package com.dy.www.beautyshow.relaxYourEye;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dy.www.beautyshow.R;


/**
 * Created by dy on 16/9/2.
 */
public class RelaxingYourEyeRecyclerViewAdapter extends RecyclerView.Adapter {


    private Context context;
    private String[] tags;
    private LayoutInflater inflater;

    RelaxingYourEyeRecyclerViewAdapter(Context context, String[] tags) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.tags = tags;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_button, parent,  false);
        return new ButtonImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ButtonImageViewHolder) {
            ((ButtonImageViewHolder) holder).btn.setText(tags[position]);
            ((ButtonImageViewHolder) holder).btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return tags.length;
    }

    public static class ButtonImageViewHolder extends RecyclerView.ViewHolder {
        Button btn;
        ButtonImageViewHolder(View view) {
            super(view);
            btn = (Button) view.findViewById(R.id.btn_tag);
        }
    }

    private ItemClickListener itemClickListener;
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
