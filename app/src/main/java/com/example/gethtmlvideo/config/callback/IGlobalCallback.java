package com.example.gethtmlvideo.config.callback;


import androidx.annotation.Nullable;

/**
 * 全局回调接口
 * */
public interface IGlobalCallback<T> {

    void executeCallback(@Nullable T args);
}
