package com.example.gethtmlvideo.net;

/**
 * Created by feng on 2018/12/6 0006
 * 网络请求时的配置信息
 */
public class RestConfig {

    private boolean isSign = true;//参数是否加密
    private boolean isUrlEncode = false;//参数是否进行urlEncode
    private boolean isOpenLogin = true;//401时是否打开登录页面

    private boolean isRequestAll = false;//直接返回全部数据

    private boolean isToastError = false;//是否toast后台返回的错误信息
    private boolean isToastAll = false;//是否toast后台返回的所有信息

    private boolean isAddTokenHeader = false;//请求头中是否添加token

    private boolean isCallbackData = true;//请求结束是否通过ISuccess返回数据


    private static final RestConfig INSTANCE = new RestConfig();

    public static RestConfig getInstance() {
        return INSTANCE;
    }

    /**
     * 属性重置
     * 在创建新的网络请求前重置
     */
    public void init() {
        isSign = true;
        isUrlEncode = false;
        isOpenLogin = true;
        isRequestAll = false;
        isToastError = false;
        isToastAll = false;
        isAddTokenHeader = true;
        isCallbackData = true;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public boolean isUrlEncode() {
        return isUrlEncode;
    }

    public void setUrlEncode(boolean urlEncode) {
        isUrlEncode = urlEncode;
    }

    public boolean isOpenLogin() {
        return isOpenLogin;
    }

    public void setOpenLogin(boolean openLogin) {
        isOpenLogin = openLogin;
    }

    public boolean isRequestAll() {
        return isRequestAll;
    }

    public void setRequestAll(boolean requestAll) {
        isRequestAll = requestAll;
    }

    public boolean isToastError() {
        return isToastError;
    }

    public void setToastError(boolean toastError) {
        isToastError = toastError;
    }

    public boolean isToastAll() {
        return isToastAll;
    }

    public void setToastAll(boolean toastAll) {
        isToastAll = toastAll;
    }

    public boolean isAddTokenHeader() {
        return isAddTokenHeader;
    }

    public void setAddTokenHeader(boolean addTokenHeader) {
        isAddTokenHeader = addTokenHeader;
    }

    public boolean isCallbackData() {
        return isCallbackData;
    }

    public void setCallbackData(boolean callbackData) {
        isCallbackData = callbackData;
    }


}
