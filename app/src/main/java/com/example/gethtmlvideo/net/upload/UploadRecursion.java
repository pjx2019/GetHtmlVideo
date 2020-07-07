package com.example.gethtmlvideo.net.upload;

import android.app.Activity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2019/2/25 0025
 */
public class UploadRecursion {

    private Activity mActivity;
    private IUploadRecursionListener mListener;
    private IUploadItemListener mListenerItem;
    private List<String> mImagePathList = new ArrayList<>();
    private List<String> mImageUrlList = new ArrayList<>();
    private int mSize;

    private UploadRecursion(Activity activity) {
        this.mActivity = activity;
    }

    public static UploadRecursion create(Activity activity) {
        return new UploadRecursion(activity);
    }

    public void upload(List<String> imagePaths, IUploadRecursionListener listener) {
        this.mListener = listener;

        if (imagePaths != null && imagePaths.size() > 0) {
            mSize = imagePaths.size();
            uploadRecursion(imagePaths, 0);
        }

    }

    //递归上传
    private void uploadRecursion(final List<String> imagePaths, final int position) {
        if (position < mSize) {
            final String imagePath = imagePaths.get(position);

            UploadFileUtils.create(mActivity)
                    .upload(imagePath, new IUploadCallback() {
                        @Override
                        public void onUploadCallback(String imageUrl) {
                            if (!TextUtils.isEmpty(imageUrl)) {
                                mImagePathList.add(imagePath);
                                mImageUrlList.add(imageUrl);

                                if (mListenerItem != null) {
                                    mListenerItem.onCallBack(imagePath, imageUrl);
                                }
                            } else {

                            }

                            if (position == mSize - 1) {
                                if (mListener != null) {
                                    mListener.callBack(mImagePathList, mImageUrlList);
                                }
                            } else {
                                uploadRecursion(imagePaths, position + 1);
                            }


                        }
                    });

        }
    }

    public UploadRecursion setUploadOneListener(IUploadItemListener listener) {
        this.mListenerItem = listener;
        return this;
    }
}
