package com.example.gethtmlvideo.utils;

import android.widget.Toast;

import com.example.gethtmlvideo.config.LemonConfig;

/**
 * Created by feng on 2017/12/19.
 * 注：如果使用activity的context，在activity销毁时会造成内存泄漏
 */

public class ToastUtils {
    protected static final String TAG = "AppToast";
    public static Toast toast;

    /**
     * 信息提示
     *
     * @param text
     */
    public static void showText(CharSequence text) {

        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(LemonConfig.getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongText(CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(LemonConfig.getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showText(int resId) {
        try {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(LemonConfig.getApplicationContext(), resId, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
        }
    }

    public static void showLongText(int resId) {
        try {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(LemonConfig.getApplicationContext(), resId, Toast.LENGTH_LONG);
            toast.show();

        } catch (Exception e) {
        }
    }
}
