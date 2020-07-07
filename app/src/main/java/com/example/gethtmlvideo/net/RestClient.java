package com.example.gethtmlvideo.net;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.example.gethtmlvideo.config.ConfigKeys;
import com.example.gethtmlvideo.config.LemonConfig;
import com.example.gethtmlvideo.loader.LoaderStyle;
import com.example.gethtmlvideo.net.callback.IError;
import com.example.gethtmlvideo.net.callback.ISuccess;
import com.example.gethtmlvideo.net.callback.RequestCallBack;
import com.example.gethtmlvideo.utils.LogUtils;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class RestClient {

    private String mUrl;
    private ISuccess iSuccess;
    private IError iError;

    private WeakHashMap<String, Object> mParams = RestCreator.getParams();
    private RequestBody mRequestBody;

    private Context mContext;
    private LoaderStyle mLoaderStyle;

    private String mDownloadDir;
    private String mExtension;
    private String mName;

    private File mFile;

    public RestClient(String mUrl, Map<String, Object> params, RequestBody requestBody,
                      ISuccess iSuccess, IError iError, Context mContext, LoaderStyle loaderStyle,
                      String mDownloadDir, String mExtension, String mName, File file) {
        this.mParams.putAll(params);
        this.mUrl = mUrl;
        this.iSuccess = iSuccess;
        this.iError = iError;
        this.mContext = mContext;
        this.mLoaderStyle = loaderStyle;
        this.mDownloadDir = mDownloadDir;
        this.mExtension = mExtension;
        this.mName = mName;
        this.mRequestBody = requestBody;
        this.mFile = file;


        for (String key : params.keySet()) {
            Object object = params.get(key);//得到每个key对应的value值
            if (object == null) {
                params.put(key, "");
            }
            LogUtils.d("net key: " + key + ", value: " + object);
        }
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    /**
     * 发起请求
     * */
    private void request(String httpMethod) {
        RestService service = RestCreator.getRestService();
        Call<String> call = null;


        if (mLoaderStyle != null) {
//            LemonLoader.showLoading(mContext);
        }

        if (RestConfig.getInstance().isSign()) {
            String appId = LemonConfig.getConfiguration(ConfigKeys.APP_ID);
//            mParams.put(BaseParameterKeys.APP_ID, appId);
//            mParams.put(BaseParameterKeys.TIMESTAMP, TimestampUtils.getTimeNow());
            //加密
//            String sign = getSign(mParams);
//            mParams.put(BaseParameterKeys.SIGN, sign);
        }

        switch (httpMethod) {
            case HttpMethod.GET:
                LogUtils.d("net get请求：" + mParams);
                call = service.get(mUrl, mParams);
                break;
            case HttpMethod.POST:
                LogUtils.d("net post请求：" + mParams);
                call = service.post(mUrl, mParams);
                break;
            case HttpMethod.POST_JSON:
                String json=new JSONObject(mParams).toString();
                LogUtils.d("net postJson请求：" + json);
                call = service.postJson(mUrl, json);
                break;
            case HttpMethod.POST_RAW:
                LogUtils.d("net postRaw请求：" + mRequestBody);
                call = service.postRaw(mUrl, mRequestBody);
                break;
            case HttpMethod.UPLOAD:
                final RequestBody formBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), mFile);

                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", mFile.getName(), formBody);
                call = service.upload(mUrl, body);
                break;
            default:
                break;
        }

        if (call != null) {
            call.enqueue(getRequestCallBack());
        }

    }


    private Callback<String> getRequestCallBack() {
        return new RequestCallBack(iSuccess, iError, mLoaderStyle);
    }


    public void get() {
        request(HttpMethod.GET);
    }


    public final void post() {
        if (mRequestBody == null) {
            request(HttpMethod.POST);
        } else {
            if (!mParams.isEmpty()) {
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void postJson() {
        request(HttpMethod.POST_JSON);
    }


    public final void upload() {
        request(HttpMethod.UPLOAD);
    }

    public final void uploadWithParams() {
        request(HttpMethod.UPLOAD_WITH_PARAMS);
    }


    private RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), value);
        return body;
    }



    /**
     * 生成签名
     *
     * @param map
     * @return
     */
    private String getSign(Map<String, Object> map) {

        String result = "";
        try {
            //ascii排序
            List<Map.Entry<String, Object>> infoIds = new ArrayList<>(map.entrySet());
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            StringBuilder sb = new StringBuilder();
            int count = 0;
            // 拼接
            for (Map.Entry<String, Object> item : infoIds) {
                String key = item.getKey();
                Object val = item.getValue();

                if (!TextUtils.isEmpty(key) && val != null && !val.toString().equals("")) {
                    if (count == 0) {
                        count++;
                    } else {
                        sb.append("&");
                    }
                    String value;
                    if (RestConfig.getInstance().isUrlEncode()) {
                        value = URLEncoder.encode(val.toString(), "utf-8");
                    } else {
                        value = val.toString();
                    }

                    sb.append(key).append("=").append(value);
                }
            }
            result = sb.toString();
//            String secret = LemonConfig.getConfiguration(ConfigKeys.APP_SECRET);
            //hmac-sha1加密
//            result = EncryptUtils.genHMAC(result, secret);
        } catch (Exception e) {
            return null;
        }
        return result;
    }


}
