package com.paraken.demo.Util;


import com.paraken.demo.R;

import java.io.File;

public class Constant {

    /**
     * 视频保存文件夹路径
     */
    public static final String MEDIA_STORE_PATH = "DCIM" + File.separator + "Camera";

    /**
     * 水印本地路径，文件必须为rgba格式的PNG图片
     * 1, "assets://image.png";
     * 2, "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     */
    public static String WATER_MARK_PATH = "drawable://" + R.drawable.watermarker;
    public static final String APP_KEY = "1477470870";
    public static final String APP_CODE = "8aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    public static final String APP_SECRET = "68dfd4d5e5f63537c7e4da6b9cfc0671cf649ae6";
}
