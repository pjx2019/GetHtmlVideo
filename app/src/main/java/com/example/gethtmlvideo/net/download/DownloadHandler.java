package com.example.gethtmlvideo.net.download;

import android.os.AsyncTask;


import com.example.gethtmlvideo.loader.LoaderStyle;
import com.example.gethtmlvideo.net.RestCreator;
import com.example.gethtmlvideo.net.callback.IError;
import com.example.gethtmlvideo.net.callback.ISuccess;
import com.example.gethtmlvideo.utils.LogUtils;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DownloadHandler {

    private String mUrl;
    private static final WeakHashMap<String, Object> mParams = RestCreator.getParams();
    private ISuccess mISuccess;
    private IError mIError;
    private LoaderStyle mLoaderStyle;
    private String mDownloadDir;
    private String mExtension;
    private String mName;

    public DownloadHandler(String mUrl, ISuccess iSuccess, IError iError, LoaderStyle loaderStyle,
                           String mDownloadDir, String mExtension, String mName) {
        this.mUrl = mUrl;
        this.mISuccess = iSuccess;
        this.mIError = iError;
        this.mLoaderStyle = loaderStyle;
        this.mDownloadDir = mDownloadDir;
        this.mExtension = mExtension;
        this.mName = mName;
    }


    public void handlerDownload() {

        RestCreator.getRestService().download(mUrl, mParams)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        LogUtils.d("download onResponse");
                        stopLoader();
                        if (response.isSuccessful()) {
                            final ResponseBody responseBody = response.body();
                            final SaveFileTask task = new SaveFileTask(mISuccess);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                    mDownloadDir, mExtension, responseBody, mName);
                        } else {
                            if (mIError != null) {
                                mIError.onError(response.code(), response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LogUtils.d("download onFailure");
                        if (mIError != null) {
                            mIError.onError(400, "http请求失败");
                        }
                        stopLoader();

                    }


                });
    }

    private void stopLoader() {
        if (mLoaderStyle != null) {
//            LemonLoader.stopLoading();
        }
    }

}
