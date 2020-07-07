package com.example.gethtmlvideo.utils;

/**
 * Created by feng on 2018/8/9 0009.
 */

public interface UrlKeys {

    /**
     * 基础url
     */
    String BASE_URL_DEV = "https://www.wanandroid.com/";//测试域名
    String BASE_URL = "http://api.yz.gydsh.com/app/";//正式版域名
    String BASE_URL_LOCAL = "http://192.168.1.105:8080/IPTV_web_war_exploded/";//本地域名

    //测试图片
    String TEST_IMAGE_URL_1 = "http://pic1.win4000.com/wallpaper/a/59bb8380d0be4.jpg";
    String TEST_IMAGE_URL_2 = "http://pic1.win4000.com/wallpaper/2017-11-06/59ffc5d231064.jpg";
    String TEST_IMAGE_URL_3 = "http://img1.gtimg.com/auto/pics/hv1/170/170/2155/140172395.jpg";
    String TEST_IMAGE_URL_4 = "http://dealer2.autoimg.cn/dealerdfs/g30/M07/7D/2C/620x0_1_q87_autohomedealer__ChcCSV2Vlf-AGCiJAAhgt8SfHuA456.jpg";
    String TEST_IMAGE_URL_5 = "http://n.sinaimg.cn/auto/transform/299/w660h439/20190421/y46U-hvvuiyn4538843.jpg";
    String TEST_IMAGE_URL_6 = "http://i8.hexun.com/2017-08-28/190609301.jpg";
    //测试视频
    String TEST_VIDEO_URL_1 = "http://video.i.haierzhongyou.com/dz/msjt/035b19d8f5ef4fba902fa00004366a19.mp4";
    String TEST_VIDEO_URL_2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
    /**
     * 配置相关
     */
    String FILE_UPLOAD = "https://s.weflys.com/api/g/upload";//文件上传
    String VERSION_UPDATE = "version/update";//版本更新
    String GENERATE_SEND ="sms/send";
    String GENERATE_VERIFY="sms/verify";

    /**
     * 首页
     */
    String USER_LOGIN="user/login";
    String INDEX_ALL ="index/all";
    String INDEX_DOWNLOAD ="index/downLoad";
    String INDEX_SECONDARY ="index/secondary";
    String INDEX_SEARCH ="index/search";
    /**
     * 详情
     */
    String INDEX_DETAIL="index/detail";
    String INDEX_DETAIL_COLLECT="collect/collectSure";
    /**
     * 我的
     */
    String INDEX_MY_LOOK_HISTORY="index/my/lookHistory";
    String INDEX_MY_COLLECT_NOTE="collect/collectNote";
    String INDEX_MY_SUBSCRIBE="index/my/subscribe";
    String INDEX_MY_PROMO_CODE="index/my/promoCode";

    String INDEX_MY_COLLECT_DELETE="collect/delete";
    String INDEX_MY_COLLECT_DELETE_ALL="collect/deleteAll";



}
