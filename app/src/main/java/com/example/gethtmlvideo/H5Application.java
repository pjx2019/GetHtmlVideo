package com.example.gethtmlvideo;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.example.gethtmlvideo.config.LemonConfig;
import com.example.gethtmlvideo.utils.UrlKeys;


/**
 * Created by feng on 2018/9/26 0026 11:43
 */
public class H5Application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        LemonConfig.init(this)
                .withApiHost(UrlKeys.BASE_URL_LOCAL)
                .withIsDebug(true)
//                .withAppId("e807f1fcf82d132f9bb018ca6738a19f")
                .withAppSecret("f82d132f9bb018ca")
                .withWeChatAppId("wx7e65dcad3f55bbed")
                .withWeChatAppSecret("689cc986dfaa2ab9090ae73f30629442")
                .configure();



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
