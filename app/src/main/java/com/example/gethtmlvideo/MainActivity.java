package com.example.gethtmlvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gethtmlvideo.net.RestClient;
import com.example.gethtmlvideo.net.callback.ISuccess;
import com.example.gethtmlvideo.utils.PermissionPageUtils;
import com.example.gethtmlvideo.utils.excel.ExcelEntity;
import com.example.gethtmlvideo.utils.excel.ExcelUtils;
import com.example.gethtmlvideo.utils.LogUtils;
import com.example.gethtmlvideo.utils.ToastUtils;
import com.example.gethtmlvideo.utils.UrlKeys;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private EditText mEtContent;
    private Button mBtnDown;
    private Button mBtnExcel;
    private Button mBtnUrl;
    private ExcelUtils excelUtils;
    private List<ExcelEntity> list;
    private List<ExcelEntity> excels = new ArrayList<>();
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean isSuccess=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        excelUtils = ExcelUtils.create();
        mEtContent = findViewById(R.id.etContent);
        mBtnDown = findViewById(R.id.btn_down);
        mBtnExcel = findViewById(R.id.btn_excel);
        mBtnUrl = findViewById(R.id.btn_url);

        mEtContent.setText("X:\\下载\\4s\\");

        mBtnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("OnClickListener=");
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, 130);
                    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (!getPackageManager().canRequestPackageInstalls()) {
                            PermissionPageUtils.create(MainActivity.this,"com.example.gethtmlvideo").goIntentSetting();//goHuaWeiMainagerAlone();
                            ToastUtils.showLongText("点击权限，允许【应用内安装其他应用】该权限，才可使用！");
                        } else {
                            intentSelect();
                        }
                    }
                } else {
                    intentSelect();
                }
            }
        });
        mBtnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnUrl.setVisibility(View.INVISIBLE);
                if (list != null) {
                    LogUtils.d("excel里面的url：" + list.size());
                    excels.clear();
                    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
                    for (int i = 0; i < list.size(); i++) {
                        ExcelEntity entity = list.get(i);
                        String title = entity.getCarType();
                        String url = entity.getVideoUrl();
                        if (!TextUtils.isEmpty(url) && url.contains("http")) {
                            getDataByJsoup(url, title,cachedThreadPool);
                        }
                    }
                } else {
                    ToastUtils.showText("解析中...");
                }
            }
        });
        mBtnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnUrl.setVisibility(View.VISIBLE);
                if (!excels.isEmpty()) {
                    LogUtils.d("视频url：" + excels.size());
                    downLoadVideo(excels);
                } else {
                    ToastUtils.showText("二次解析中...");
                }
            }
        });
    }
    //解析url
    private void getDataByJsoup(final String h5Url, final String title,ExecutorService cachedThreadPool) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 网络加载HTML文档
                    Document doc = Jsoup.connect(h5Url)
                            .timeout(0) // 设置超时时间
                            .maxBodySize(0)
                            .get(); // 使用GET方法访问URL
                    Elements elements = doc.select("div.hkplayer");
                    String url = elements.select("video").attr("src"); // 链接

                    ExcelEntity entity = new ExcelEntity();
                    entity.setCarType(title);
                    entity.setVideoUrl(url);
                    excels.add(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //解析url
    private void getDataByJsoup(final String h5Url, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 网络加载HTML文档
                    Document doc = Jsoup.connect(h5Url)
                            .timeout(0) // 设置超时时间
                            .maxBodySize(0)
                            .get(); // 使用GET方法访问URL
//                    Element bodyData = doc.body();
                    Elements elements = doc.select("div.hkplayer");
                    String url = elements.select("video").attr("src"); // 链接

                    ExcelEntity entity = new ExcelEntity();
                    entity.setCarType(title);
                    entity.setVideoUrl(url);
                    excels.add(entity);
//                    Elements elementsTitle = doc.select("div.videoinfo");
//                    String title = elementsTitle.select("h2").text();

//                    downLoadVideo(title, url);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /***********下载到指定文件夹中*************/
    private void downLoadVideo(List<ExcelEntity> list) {
        RestClient.builder()
                .url(UrlKeys.INDEX_DOWNLOAD)
                .params("list", list)
                .params("dir", mEtContent.getText().toString().trim())
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(response);
                    }
                }).build().postJson();
    }

    private void intentSelect() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("onActivityResult=" );
        if (requestCode == 1 && data != null) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = uri.getPath();
                    if (path != null) {
                        path = path.substring(path.indexOf(":") + 1, path.length());
                        File file = new File(path);
                        LogUtils.d("file=" + file + file.exists());

                        if (excelUtils.checkIfExcelFile(file)) {
                            if (file.exists()) {
                                try {
                                    list = excelUtils.readExcel(file);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            ToastUtils.showText("不是Excel文件");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 130) {
            for (int i = 0; i < permissions.length; i++) {
                LogUtils.d("申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
                if (grantResults[i] != 0) {
                    isSuccess =false;
                }
            }
            if (isSuccess) {
                intentSelect();
            }else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
                PermissionPageUtils.create(this,"com.example.gethtmlvideo").goIntentSetting();
                ToastUtils.showLongText("点击权限，允许[应用内安装其他应用]该权限，才可使用！");
            }
        }
    }

}
