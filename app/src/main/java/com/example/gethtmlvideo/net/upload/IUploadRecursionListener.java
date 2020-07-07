package com.example.gethtmlvideo.net.upload;

import java.util.List;

/**
 * Created by feng on 2019/2/25 0025
 */
public interface IUploadRecursionListener {

    void callBack(List<String> imagePaths, List<String> imageUrls);
}
