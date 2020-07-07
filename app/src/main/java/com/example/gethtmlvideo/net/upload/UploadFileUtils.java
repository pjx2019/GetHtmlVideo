package com.example.gethtmlvideo.net.upload;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.gethtmlvideo.net.RestClient;
import com.example.gethtmlvideo.net.callback.IError;
import com.example.gethtmlvideo.net.callback.ISuccess;


/**
 * Created by feng on 2018/7/31 0031.
 */

public class UploadFileUtils {

    private Context mContext;
    private UploadFileUtils(Context context){
        this.mContext = context;
    }

    public static UploadFileUtils create(Context context){
        return new UploadFileUtils(context);
    }

    public void upload(String file, final IUploadCallback callback){
        RestClient.builder()
                .isSignParams(false)
                .addTokenHeader()
                .url("")
                .file(file)
                .loader(mContext)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response).getJSONObject("data");
                        String imageUrl = object.getString("url");
                        if(callback != null){
                            callback.onUploadCallback(imageUrl);
                        }
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        if(callback != null){
                            callback.onUploadCallback("");
                        }
                    }
                })
                .build().uploadWithParams();
    }


}
