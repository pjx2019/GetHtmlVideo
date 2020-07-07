package com.example.gethtmlvideo.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;


import com.example.gethtmlvideo.config.LemonConfig;
import com.example.gethtmlvideo.net.callback.ISuccess;
import com.example.gethtmlvideo.utils.FileUtil;
import com.example.gethtmlvideo.utils.LogUtils;
import com.example.gethtmlvideo.utils.UriUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;


public class SaveFileTask extends AsyncTask<Object, Void, File> {

    private ISuccess mISuccess;

    public SaveFileTask(ISuccess mISuccess) {
        this.mISuccess = mISuccess;
    }

    @Override
    protected File doInBackground(Object... params) {
        String downloadDir = (String) params[0];
        String extension = (String) params[1];
        final ResponseBody body = (ResponseBody) params[2];
        final String name = (String) params[3];
        final InputStream is = body.byteStream();
        if (downloadDir == null || downloadDir.equals("")) {
            downloadDir = "down_loads";
        }
        if (extension == null || extension.equals("")) {
            extension = "";
        }

        return writeToDisk(body.contentLength(), is, "down_load", name);
//        if (name == null) {
//            return FileUtil.writeToDisk(is, downloadDir, extension.toUpperCase(), extension);
//        } else {
//            return FileUtil.writeToDisk(is, downloadDir, name);
//        }

    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (mISuccess != null) {
            mISuccess.onSuccess(file.getPath());
        }

        autoInstallApk(file);
    }


    private File writeToDisk(long length, InputStream is, String dir, String name) {
        File file = FileUtil.createFile(dir, name);
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

//            NotificationManager manager = (NotificationManager) LemonConfig.getApplicationContext()
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            Notification.Builder builder = null;
//            if (manager != null) {
//                builder = new Notification.Builder(LemonConfig.getApplicationContext());
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    String channelId = "gydsh";
//                    NotificationChannel notificationChannel = new NotificationChannel(channelId, "version", NotificationManager.IMPORTANCE_HIGH);
//                    notificationChannel.enableVibration(false);
//                    notificationChannel.setSound(null, null);
//
//                    manager.createNotificationChannel(notificationChannel);
//                    builder.setChannelId(channelId);
//                }
//
//                builder.setContentTitle("版本更新");
//                builder.setSmallIcon(R.mipmap.icon_logo);
//                builder.setAutoCancel(false);//设置通知被点击一次是否自动取消
//                builder.setOnlyAlertOnce(true);
//            }

            byte data[] = new byte[1024 * 4];
            int count;
            int writeLength = 0;
            while ((count = bis.read(data)) != -1) {
                bos.write(data, 0, count);
                writeLength += count;
                int progress = (int) (writeLength * 100 / length);
                LogUtils.d("Task progress: " + progress + ",writeLength: " + writeLength + ",length:" + length);

//                if (builder != null) {
//                    if (progress == 100) {
//                        builder.setContentText("下载完成：" + progress + "%");
////                        manager.cancel(1);
//                    } else {
//                        builder.setContentText("下载中：" + progress + "%");
//                    }
//
//                    builder.setProgress(100, progress, false);
//                    manager.notify(1, builder.build());
//                }

            }

            bos.flush();
            fos.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    private void autoInstallApk(File file) {
        final Intent intent = new Intent();
        String type = "application/vnd.android.package-archive";
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(UriUtils.getUriForFile(LemonConfig.getApplicationContext(), file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            if (writeAble) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }

        LemonConfig.getApplicationContext().startActivity(intent);
    }
}
