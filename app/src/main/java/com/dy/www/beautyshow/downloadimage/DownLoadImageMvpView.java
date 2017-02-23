package com.dy.www.beautyshow.downloadimage;

import com.dy.www.beautyshow.base.MvpView;

import java.util.List;
import java.util.Map;



/**
 * Created by dy on 16/7/26.
 */
public interface DownLoadImageMvpView extends MvpView {
    void setImages(List<Map<String, Object>> list);
}
