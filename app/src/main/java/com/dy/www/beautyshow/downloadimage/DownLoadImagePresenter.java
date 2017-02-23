package com.dy.www.beautyshow.downloadimage;

import com.dy.www.beautyshow.base.BasePresenter;
import com.dy.www.beautyshow.common.ImageCommonKey;
import com.dy.www.beautyshow.remote.ImageService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by dy on 16/7/26.
 */
public class DownLoadImagePresenter extends BasePresenter<DownLoadImageMvpView> {

    private ImageService imageService;
    private Subscription subscription;

    @Inject
    DownLoadImagePresenter(ImageService imageService) {
        this.imageService = imageService;
    }

    public void getNews(String tag) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = imageService.getImages("美女", tag, 0, 100)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("imgs");
                        getMvpView().setImages(handleJsonArray(jsonArray));
                    }
                });
    }

    private List<Map<String, Object>> handleJsonArray (JsonArray jsonArray) {
        List<Map<String, Object>> list =  new ArrayList<>();
        for (int i = 0 ;i < 50 && i < jsonArray.size() ; i++) {
            JsonObject jsonObject = (JsonObject)jsonArray.get(i);
            Map<String, Object> map = new HashMap<>();
            if (jsonObject.has(ImageCommonKey.TITLE)) {
                map.put(ImageCommonKey.TITLE, jsonObject.get(ImageCommonKey.TITLE).getAsString());
            }

            if (jsonObject.has(ImageCommonKey.SOURCEURL)) {
                map.put(ImageCommonKey.SOURCEURL, jsonObject.get(ImageCommonKey.SOURCEURL).getAsString());
            }

            if (jsonObject.has(ImageCommonKey.HEIGHT)) {
                map.put(ImageCommonKey.HEIGHT, jsonObject.get(ImageCommonKey.HEIGHT).getAsString());
            }

            if (jsonObject.has(ImageCommonKey.WIDTH)) {
                map.put(ImageCommonKey.WIDTH, jsonObject.get(ImageCommonKey.WIDTH).getAsString());
            }

            if (jsonObject.has("desc")) {
                map.put("title", jsonObject.get("desc").getAsString());
            }
            if (jsonObject.has("imageUrl")) {
                map.put(ImageCommonKey.SOURCEURL, jsonObject.get("imageUrl").getAsString());
            }
            if (!map.isEmpty()) {
                list.add(map);
            }
        }

        return list;
    }
}
