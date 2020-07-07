package com.example.gethtmlvideo.net.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.gethtmlvideo.config.callback.CallbackKey;
import com.example.gethtmlvideo.config.callback.CallbackManager;
import com.example.gethtmlvideo.config.callback.IGlobalCallback;
import com.example.gethtmlvideo.loader.LoaderStyle;
import com.example.gethtmlvideo.net.RestConfig;
import com.example.gethtmlvideo.utils.LogUtils;
import com.example.gethtmlvideo.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestCallBack implements Callback<String> {

    private ISuccess iSuccess = null;
    private IError iError = null;
    private LoaderStyle mLoaderStyle;
    private boolean isToastError = false;
    private boolean isToastAll = false;
    private boolean isRequestAll;//是否不处理请求结果，将参数直接返回
    private boolean isOpenLogin;
    private boolean isCallbackData;
//    private boolean isEntryLogin;//401时是否跳转到登录页面，默认跳转

    public RequestCallBack(ISuccess success, IError error,
                           LoaderStyle loaderStyle) {
        this.iSuccess = success;
        this.iError = error;
        this.mLoaderStyle = loaderStyle;

        isToastError = RestConfig.getInstance().isToastError();
        isToastAll = RestConfig.getInstance().isToastAll();
        isRequestAll = RestConfig.getInstance().isRequestAll();
        isOpenLogin = RestConfig.getInstance().isOpenLogin();
        isCallbackData = RestConfig.getInstance().isCallbackData();
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {

        LogUtils.d("net: " + response);
        LogUtils.d("net 请求成功：" + response.body());
        stopLoading();

        if (!isCallbackData) {
            ToastUtils.showText("请求失败");
            return;
        }


        if (response.isSuccessful() && call.isExecuted()) {
            //直接返回全部数据---注：常用于json数据中没有“status”字段的情况
            if (isRequestAll) {
                if (iSuccess != null) {
                    iSuccess.onSuccess(response.body());
                }
                return;
            }

            JSONObject object = JSON.parseObject(response.body());
            if (object == null) return;
            int status = -1;
            try {

                status = object.getInteger("code");
//                status = object.getInteger("code");
            } catch (Exception e) {
                LogUtils.d("接口状态码，解析不对！");
                ToastUtils.showText("接口状态码，解析不对！");
            }
            //请求成功
//            if (status == 200) {
            if (status == 200) {
                if (iSuccess != null) {
                    iSuccess.onSuccess(response.body());
                }
                if (isToastAll) {
                    String message = object.getString("msg");
//                    String message = object.getString("msg");
                    ToastUtils.showText(message);
                }
            } else {
                String message = object.getString("msg");
//                String message = object.getString("msg");
                if (isToastError || isToastAll) {
                    ToastUtils.showText(message);
                }
                if (iError != null) {
                    iError.onError(status, message);
                }
            }
        } else {
            if (iError != null) {
                iError.onError(response.code(), "HTTP请求错误信息: " + response.message());
            }

            if (response.code() == 401) {
                IGlobalCallback callbackLogin = CallbackManager.getInstance().getCallback(CallbackKey.LOGIN_OUT);
                if (callbackLogin != null && isOpenLogin) {
                    callbackLogin.executeCallback("");
                }
            }
//onResponse: Response{protocol=http/1.1, code=401, message=Unauthorized, url=https://t.udian2.com/api/app/member/info?sign=4dd5c2ebe0d4e556e919ce21b1ea69c59ca5c936&appid=e807f1fcf82d132f9bb018ca6738a19f&_t=1537364760}
        }

    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        LogUtils.d("net onFailure: " + t.getMessage());
        stopLoading();

        if (iError != null) {
            iError.onError(401, "HTTP请求错误信息: onFailure");
            LogUtils.d("net onError: ");
        }

        ToastUtils.showText("请求失败,请检查你的网络");


    }

    private void stopLoading() {
        if (mLoaderStyle != null) {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//            LemonLoader.stopLoading();
//                }
//            },1000);
        }
    }
}
