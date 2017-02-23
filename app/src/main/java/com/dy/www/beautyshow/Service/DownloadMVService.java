package com.dy.www.beautyshow.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscription;

/**
 * Created by dy on 2017/2/15.
 */

public class DownloadMVService extends Service {

//    private DownloadServiceBinder binder;
    private String sort = "totalViews";
    private String area = "KR";
    private Subscription subscription;
    private Context context;
    private List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getMVUrl();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public class DownloadServiceBinder extends Binder {
//        public void startDownloadMV () {
//            downloadMv();
//        }
//    }

    private void getMVUrl() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("http://mv.yinyuetai.com/all?sort=totalViews&area=KR")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String htmlStr = response.body().string();
                Document document = Jsoup.parse(htmlStr);
                String itemTag = "div[class=info]";

                String linkTag = "a";

                Elements items = document.select(itemTag);

                Elements links = items.select(linkTag);

                for(Element l : links) {
                    Map<String, Object> map = new HashMap<>();
                    String href = l.attr("abs:href");//完整Href
                    String absHref = l.attr("href");//相对路径
                    map.put("url", absHref);
                    String text = l.text();
                    String title = l.attr("title");
                    String imgUrl = l.attr("src");
                    map.put("imgUrl", imgUrl);
                    map.put("title", title);
                    list.add(map);
                }
                playVideoRandom();
            }
        });
    }

    private void playVideoRandom() {
        if (list.size() > 0) {
            int i = new Random().nextInt(list.size());
            final Map<String, Object> map = list.get(i);
//            //创建okHttpClient对象
//            OkHttpClient mOkHttpClient = new OkHttpClient();
//            //创建一个Request
//            final Request request = new Request.Builder()
//                    .url("http://dd.yinyuetai.com/uploads/videos/common/44960139807B7DBCA60D66156BB7C8DE.mp4")
//                    .build();
//            //new call
//            Call call = mOkHttpClient.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    int contentLength = Integer.valueOf(response.header("Content-Length", "-1"));
//                    InputStream inputStream = response.body().byteStream();
//                    ImageUtil.saveToSD(context, inputStream, "dy_open", map.get("title").toString(), contentLength);
//                }
//            });
        }
    }
}
