package com.dy.www.beautyshow.remote;

import android.content.Context;

import com.google.gson.JsonObject;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static org.jsoup.Connection.Method.GET;

/**
 * Created by dy on 16/9/1.
 */
public interface ImageService {

    //String ENDPOINT = "http://n.xxt.cn:3000/";
    //String ENDPOINT = "http://api.laifudao.com/";
    String ENDPOINT = "http://image.baidu.com/";

    @GET("/data/imgs?tag=性感&sort=0&pn=10&rn=50&p=channel&from=1")
    Observable<JsonObject> getImages(@Query("col") String col,
                                     @Query("tag") String tag,
                                     @Query("pn") int start,
                                     @Query("rn") int end);

    class Creator {
        public static ImageService getService(Context context) {
            Retrofit retrofit = RemoteUtil.createRetrofitInstance(context, ENDPOINT);
            return retrofit.create(ImageService.class);
        }
    }
}
