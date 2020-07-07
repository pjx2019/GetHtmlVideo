package com.example.gethtmlvideo.net;


import android.text.TextUtils;


import com.example.gethtmlvideo.config.ConfigKeys;
import com.example.gethtmlvideo.config.LemonConfig;

import java.io.IOException;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RestCreator {


    private static final class ParamsHolder {
        public static final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    }

    public static WeakHashMap<String, Object> getParams() {
        return ParamsHolder.PARAMS;
    }

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }

    private static final class RetrofitHolder {
        private static final String BASE_URL = LemonConfig.getConfiguration(ConfigKeys.API_HOST);

        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_HOLDER)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    private static final class OkHttpHolder {
        //连接超时时间
        private final static int READ_TIMEOUT = 100;

        private final static int CONNECT_TIMEOUT = 60;

        private final static int WRITE_TIMEOUT = 60;

        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        //okHttpClient对象
        private static final OkHttpClient OK_HTTP_HOLDER = BUILDER
                //读取超时
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                //连接超时
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                //写入超时
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                //自定义连接池最大空闲连接数和等待时间大小，否则默认最大5个空闲连接
                .connectionPool(new ConnectionPool(32, 5, TimeUnit.MINUTES))

                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
//                        builder.addHeader("Source", "android");
//                        builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        builder.addHeader("Content-Type", "application/json; charset=utf-8");
                        Request request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }

    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

}
