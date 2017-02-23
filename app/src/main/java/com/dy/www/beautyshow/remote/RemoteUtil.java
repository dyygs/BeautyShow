package com.dy.www.beautyshow.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by dy on 16/9/1.
 */
public final class RemoteUtil {
    /** 默认请求超时设置-连接超时时间(单位：秒) */
    public static final long DEFAULT_CONNECTION_TIMEOUT_SECONDS = 30000;

    /** 默认请求超时设置-读超时时间(单位：秒) */
    public static final long DEFAULT_READ_TIMEOUT_SECONDS = 30000;

    /** 默认请求超时设置-写超时时间(单位：秒) */
    public static final long DEFAULT_WRITE_TIMEOUT_SECONDS = 30000;


    public static Retrofit createRetrofitInstance(Context context, String endPoint) {
        return createRetrofitInstance(context, endPoint,
                DEFAULT_CONNECTION_TIMEOUT_SECONDS, DEFAULT_READ_TIMEOUT_SECONDS, DEFAULT_WRITE_TIMEOUT_SECONDS);
    }



    public static Retrofit createRetrofitInstance(
            Context context,
            String endPoint,
            Long connectTimeout,
            Long readTimeoutSeconds,
            Long writeTimeoutSeconds) {

        OkHttpClient okHttpClient = getOkHttpClient(context, connectTimeout,
                readTimeoutSeconds, writeTimeoutSeconds);

        Gson gson = new GsonBuilder()

                .serializeNulls()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public static OkHttpClient getOkHttpClient(
            Context context,
            Long connectTimeout,
            Long readTimeoutSeconds,
            Long writeTimeoutSeconds) {

        return new OkHttpClient.Builder()
                .addInterceptor(new OkHttpInterceptor(context))
                .connectTimeout(
                        connectTimeout != null ? connectTimeout : DEFAULT_CONNECTION_TIMEOUT_SECONDS,
                        TimeUnit.SECONDS)
                .readTimeout(readTimeoutSeconds != null ? readTimeoutSeconds : DEFAULT_READ_TIMEOUT_SECONDS,
                        TimeUnit.SECONDS)
                .writeTimeout(writeTimeoutSeconds != null ? writeTimeoutSeconds : DEFAULT_WRITE_TIMEOUT_SECONDS,
                        TimeUnit.SECONDS)
                .build();
    }


    private static class OkHttpInterceptor implements Interceptor {

        Context context;
        public OkHttpInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder();
            // 请求接口
            Response response;
            response = chain.proceed(requestBuilder.build());
            return response;
        }
    }

    /***
     * 请求参数需要指定编码方式的请求
     * @param ctx       上下文
     * @param method    请求方法：GET、POST
     * @param url       请求url：不带查询参数的形式
     * @param parasMap  请求参数
     * @param encoding  请求参数的编码格式.当encoding为null或者""时，则不再编码
     * @return          请求结果
     */
    public static Observable<JsonObject> okhttpRequestWithEncoding(Context ctx, String method, final String url,
                                                                   Map<String, Object> parasMap, final String encoding) {
        final Context context = ctx;
        final String requestMethod = method;
        final String urlStr = url;
        final Map<String, Object> paras = parasMap;

        return Observable.create(new Observable.OnSubscribe<JsonObject>() {
            @Override
            public void call(final Subscriber<? super JsonObject> subscriber) {
                try {
                    OkHttpClient client = getOkHttpClient(context);

                    FormBody.Builder builder = new FormBody.Builder();

                    Request request;
                    if ("GET".equals(requestMethod.toUpperCase())) {
                        // GET请求
                        String urlPath = urlStr;

                        // 处理GET请求的参数
                        if (paras != null && paras.size() > 0) {
                            String parasStr = "";
                            for (String key : paras.keySet()) {

                                String keyValue = paras.get(key).toString();
                                if (encoding != null && !"".equals(encoding)) {
                                    keyValue = URLEncoder.encode(keyValue, encoding);
                                }
                                if ("".equals(parasStr)) {
                                    parasStr = connectStrings(parasStr, key, "=", keyValue);
                                } else {
                                    parasStr = connectStrings(parasStr, "&", key, "=", keyValue);
                                }
                            }

                            if (!"".equals(parasStr)) {
                                if (urlStr.contains("?")) {
                                    urlPath = connectStrings(urlPath, "&", parasStr);
                                } else {
                                    urlPath = connectStrings(urlPath, "?", parasStr);
                                }
                            }
                        }

                        request = new Request.Builder()
                                .url(urlPath)
                                .build();
                    } else {
                        // POST请求
                        URL url = new URL(urlStr);
                        String urlPath = connectStrings("http://", url.getHost(), url.getPath());

                        // 解析url中的查询参数
                        String queryStr = url.getQuery();
                        if (queryStr != null && !"".equals(queryStr)) {

                            String[] querys = queryStr.split("&");
                            for (String query : querys) {
                                if (query.contains("=")) {
                                    String[] keyValue = query.split("=");
                                    String value = keyValue[1];
                                    if (encoding != null && !"".equals(encoding)) {
                                        value = URLEncoder.encode(value, encoding);
                                    }
                                    builder.addEncoded(keyValue[0], value);
                                }
                            }
                        }

                        // 解析传递过来的查询参数
                        if (paras != null && paras.size() > 0) {
                            for (String key : paras.keySet()) {
                                String value = paras.get(key).toString();
                                if (encoding != null && !"".equals(encoding)) {
                                    value = URLEncoder.encode(value, encoding);
                                }
                                builder.addEncoded(key, value);
                            }
                        }

                        RequestBody postBody = builder.build();
                        request = new Request.Builder()
                                .url(urlPath)
                                .post(postBody)
                                .build();
                    }

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                InputStream input = response.body().byteStream();
                                String responseStr = input.toString();
                                Gson gson = new Gson();
                                JsonObject jsonObject = gson.fromJson(responseStr, JsonObject.class);
                                subscriber.onNext(jsonObject);
                                input.close();
                            } catch (IOException e) {
                                throw e;
                            } catch (Exception e) {
                                subscriber.onError(e);
                            }
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    subscriber.onError(e);
                } catch (MalformedURLException e) {
                    subscriber.onError(e);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 创建OkHttpClient实例
     * @param context
     * @return OkHttpClient
     */
    public static OkHttpClient getOkHttpClient(Context context) {
        return getOkHttpClient(context, DEFAULT_CONNECTION_TIMEOUT_SECONDS,
                DEFAULT_READ_TIMEOUT_SECONDS, DEFAULT_WRITE_TIMEOUT_SECONDS);
    }

    /**
     * 字符串拼接
     * @param subStrings 可变字符串数组
     * @return 拼接后的字符串
     */
    public static String connectStrings(String... subStrings) {
        StringBuilder sb = new StringBuilder();
        for (String str : subStrings) {
            sb.append(str);
        }
        return sb.toString();
    }
}
