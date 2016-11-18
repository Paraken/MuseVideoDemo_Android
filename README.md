[![Paraken](http://www.paraken.com/img/logo2.png "Paraken")](http://www.paraken.com/)

MuseVideo Android
---------

#### 1、简介
平行视野 MuseSDK 是一款集视频/照片拍摄和视频导入功能为一体的 SDK，提供了包括美颜、滤镜、水印、回删、按住拍、闪光灯、前后摄像头切换、对焦等功能。

- MuseSDK 标准版将以上功能集成，开发中仅需一句语句就可以调用视频拍摄或者视频导入功能。通过对参数进行配置，可以选择需要的功能。
- MuseSDK 定制版则在标准版基础上加入了更多高级功能，并且我们将根据用户需求对拍摄界面进行定制。
- MuseSDK Android标准版系统要求：Android 4.3 (Android API level 18)，设备支持OpenGL ES2.0。

#### 2、使用指南
##### 2.1、下载
[下载 SDK](http://www.paraken.com/musesdk/download/sdk/android)

##### 2.2、获取授权
使用我司 MuseSDK 需要授权。不申请授权使用SDK时仅限评估用途。请发邮件到 <feedback@paraken.com> 询问商务合作相关信息。

##### 2.3、集成
1. 拷贝 SDK 文件 musevideosdk.aar 到 ./libs 下。
2. 配置 build.gradle
```
repositories { flatDir { dirs 'libs' } }
      dependencies {
          compile fileTree(include: ['*.jar'], dir: 'libs')
          compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
          compile 'com.google.android.exoplayer:exoplayer:r1.5.9'
          compile(name: 'musevideosdk', ext: 'aar')
```

##### 2.4、参数配置
- 建议如下SDK初始化代码放在在Application中
``` java
MuseVideoManager.getInstance().init(this);
MuseVideoServiceImpl museVideoService = MuseVideoManager.getInstance().getMuseVideoImpl();
// APP_KEY, APP_CODE,  APP_SECRET数据来自我司授权．进入拍摄页或导入页前需先注册
museVideoService.register(Constant. APP_KEY, Constant.APP_CODE, Constant.APP_SECRET);
```

- 未设置选项采取默认值
``` java
VideoCreateInfo info = new VideoCreateInfo.Builder()
　　　　　//显示倒计时开关，默认true（显示）
         .setEnableCountDown(true)　
　　　　　//显示静音开关，默认true（显示）
         .setEnableMuteSwitch(true)
　　　　　//显示闪光灯开关，默认true（显示）      
　　　　 .setEnableTorchSwitch(true)       
　　　　　//显示美颜开关，默认true（显示）
         .setEnableBeautySwitch(true)      
　　　　　//显示前后摄像头切换开关，默认true（显示）
         .setEnableCameraSwitch(true)     
　　　　　 //显示美颜幅度调节开关，默认true（显示）．若美颜关, 美颜幅度调节开关不起作用
          .setEnableBeautySetProgress(true)     
　　　　　//可自定义 1~100, 默认50
         .setDefaultBeautyProgress(50)     
　　　　　//开启导入，默认true （开启）
         .setEnableImport(true)                  
　　　　　//开启编辑，默认true （开启）
         .setEnableEdit(true)     　　　　　
　　　　　//后置摄像头美颜关，默认false  （关闭）
         .setEnableBeautyBack(false)　　  
　　　　　//前置摄像头美颜开，默认true （开启）
         .setEnableBeautyFront(true)
　　　　　//视频录制码率.单位: kbps
     //1920x1080     4000 kbps(default)
     //1280x720      2000 kbps(default)
     //854x480       1000 kbps(default)
          .setVideoRate(2000)           
　　　　　 //视频录制分辨率，默认720*720. 参考 VideoResolution
          .setCaptureResolution(VideoResolution.VIDEO_720x720)  
　　　　　//拍摄模式：长按拍&s松开拍．默认松开拍．参考CaptureMode.
         .setCaptureMode(CaptureMode.MULTICLIPS_SHORT_PRESS)  
　　　　　//长按拍最小时长和最大时长，默认分别为3s和15s
         .setMultiClipsShortPressMax(3 * 1000, 15 * 1000)    
　　　　　//松开拍最小时长和最大时长，默认分别为3s和15s
         .setMultiClipsExtendedPressRange(3 * 1000, 15 * 1000)    
　　　　　//水印位置(左下，右下和无)．默认左下．参考WaterMarker
         .setWaterMarkPosition(WaterMarker.WATER_MARKER_BOTTOM_LEFT)
　　　　　//水印资源图片路径（路径格式：1, "assets://image.png";  2, "drawable://" + R.drawable.image）
         .setWaterMarkPath("drawable://" + R.drawable.image)
　　　　　//视频资源保存路径.默认"DCIM/Camera/MuseSDK"
         .setMediaStorePath("DCIM/3rdApp")
　　　　　//拍摄页面打开摄像头. 默认后置摄像头
         .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
　　　　　//导出视频最短时间．默认1s
         .setImportVideoMinDuration(1* 1000)
　　　　　// 导出视频最长时间．默认180s
         .setImportVideoMaxDuration(180 * 1000)
         .build();　
```

- 初始化配置
``` java
museVideoService.initRecord(info);
```

- 如果只需要导入功能，只需要设置导出视频最短和最长时间
``` java
VideoCreateInfo info = new VideoCreateInfo.Builder()
　　　　　//导出视频最短时间．默认1s
          .setImportVideoMinDuration(1* 1000)
　　　　　// 导出视频最长时间．默认180s
          .setImportVideoMaxDuration(180 * 1000)
          .build();
```

##### 2.5、调用
配置完成后，即可通过如下调用打开拍摄页或导入页

- 打开拍摄页面
``` java
/**
 *
 * @param context 上下文
 * @param requestCode 当前activity 中被调用onActivityResult()请求码
 */
MuseVideoManager.getInstance().getMuseVideoImpl().showRecordPage(this, CAPTURE_REQUEST);
```

- 打开导入页面
``` java
/**
  *
  * @param context 上下文
  * @param path      导入视频路径
  * @param requestCode 当前activity 中被调用onActivityResult()请求码
  */
MuseVideoManager.getInstance().getMuseVideoImpl().showImportPage(this, path, IMPORT_REQUEST);
```

- 获取最终视频路径通过Intent 传回
``` java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
        case CAPTURE_REQUEST:
            if (resultCode == RESULT_OK && data != null) {
                String outputPath = data.getStringExtra("outputPath");
            }
            break;
　       case IMPORT_REQUEST:
            if (resultCode == RESULT_OK && data != null) {
                String outputPath = data.getStringExtra("outputPath");
            }
            break;
    }
}
```
