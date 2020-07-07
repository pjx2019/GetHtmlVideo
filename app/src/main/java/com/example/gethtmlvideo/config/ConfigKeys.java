package com.example.gethtmlvideo.config;

/**
 * Created by feng on 2017/12/18.
 * 全局配置key值
 */

public interface ConfigKeys {
    /**
     * 网络请求域名
     * IS_DEBUG
     * 当前应用appId
     * 当前应用appSecret
     * 微信AppId
     * 微信AppSecret
     * 地图key
     * */
    String API_HOST = "apiHost";
    String IS_DEBUG = "isDebug";
    String APP_ID = "appId";
    String APP_SECRET = "appSecret";
    String WE_CHAT_APP_ID = "weChatAppId";
    String WE_CHAT_APP_SECRET = "weChatAppSecret";
    String MAP_KEY = "mapKey";

    /**
     * 初始化是否完成
     * app的上下文对象
     * 全局handler
     * */

    String CONFIG_READY = "configReady";
    String APPLICATION_CONTEXT = "appContext";
    String HANDLER = "handler";


    String JAVASCRIPT_INTERFACE = "javaScriptInterface";



}
