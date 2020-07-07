package com.example.gethtmlvideo.net;

import android.content.Context;

import com.example.gethtmlvideo.loader.LoaderStyle;
import com.example.gethtmlvideo.net.callback.IError;
import com.example.gethtmlvideo.net.callback.ISuccess;
import com.example.gethtmlvideo.utils.LogUtils;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class RestClientBuilder {
    private String mUrl = null;

    private static final Map<String, Object> PARAMS = RestCreator.getParams();

    private ISuccess mISuccess = null;
    private IError mIError = null;

    private Context mContext = null;

    private RequestBody mBody = null;

    private File mFile = null;
    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;

    private LoaderStyle mLoaderStyle = null;


    RestClientBuilder() {
        RestConfig.getInstance().init();//必须初始化配置
        PARAMS.clear();
    }


    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(Map<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
        return this;
    }

    public final RestClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String file) {
        LogUtils.d("net file: " + file);
        this.mFile = new File(file);
        return this;
    }

    public final RestClientBuilder dir(String downloadDir) {
        this.mDownloadDir = downloadDir;
        return this;
    }

    public final RestClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    public final RestClientBuilder name(String name) {
        this.mName = name;
        return this;
    }

    public final RestClientBuilder raw(String raw) {
        LogUtils.d("net raw: " + raw);
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }


    public final RestClientBuilder loader(Context context, LoaderStyle style) {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }

    public final RestClientBuilder loader(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }


    public final RestClientBuilder toastAll() {
        RestConfig.getInstance().setToastAll(true);
        return this;
    }

    public final RestClientBuilder toast() {
        RestConfig.getInstance().setToastError(true);
        return this;
    }


    //直接回调所有数据，不对code做判断
    public final RestClientBuilder requestAll() {
        RestConfig.getInstance().setRequestAll(true);
        return this;
    }


    //请求头中是否添加token，默认不添加
    public final RestClientBuilder addTokenHeader() {
        RestConfig.getInstance().setAddTokenHeader(true);

        return this;
    }


    //是否加密，默认加密
    public final RestClientBuilder isSignParams(boolean isSign) {
        RestConfig.getInstance().setSign(isSign);
        return this;
    }

    //加密时 value是否进行urlEncode
    public final RestClientBuilder isUrlEncode(boolean isUrlEncode) {
        RestConfig.getInstance().setUrlEncode(isUrlEncode);
        return this;
    }

    //401时 是否跳转到登录页面---默认跳转
    public final RestClientBuilder isOpenLogin(boolean isOpenLogin) {
        RestConfig.getInstance().setOpenLogin(isOpenLogin);
        return this;
    }

    public final RestClient build() {
        return new RestClient(mUrl, PARAMS, mBody, mISuccess, mIError, mContext, mLoaderStyle,
                mDownloadDir, mExtension, mName, mFile);
    }
}
