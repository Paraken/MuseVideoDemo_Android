package com.paraken.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.paraken.demo.Util.Constant;
import com.paraken.demo.widget.SliderSwitch;
import com.paraken.musevideosdk.MuseVideoManager;
import com.paraken.musevideosdk.constant.CaptureMode;
import com.paraken.musevideosdk.constant.VideoResolution;
import com.paraken.musevideosdk.constant.WaterMarker;
import com.paraken.musevideosdk.impl.MuseVideoServiceImpl;
import com.paraken.musevideosdk.session.VideoCreateInfo;

public class CaptureConfigActivity extends Activity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        SliderSwitch.OnSwitchChangedListener {
    private static final String TAG = "ConfigActivity";
    private static final int CAPTURE_REQUEST = 1;

    /**
     * 松开拍最小时间最小值.  用户可以自己设置．　此处只是展示该功能
     */
    private static int MIN_RELEASE_FROM = 3 * 1000;
    /**
     * 松开拍最小时间最大值.
     */
    private static int MIN_RELEASE_TO = 8 * 1000;

    /**
     * 松开拍最大时间最小值.
     */
    private static int MAX_RELEASE_FROM = 15 * 1000;
    /**
     * 松开拍最大时间最大值.
     */
    private static int MAX_RELEASE_TO = 180 * 1000;

    /**
     * 按住拍最小时间最小值.  用户可以自己设置．　此处只是展示该功能
     */
    private static int MIN_PRESS_FROM = 3 * 1000;
    /**
     * 按住拍最小时间最大值.
     */
    private static int MIN_PRESS_TO = 8 * 1000;
    /**
     * 按住拍最大时间最小值.
     */
    private static int MAX_PRESS_FROM = 15 * 1000;
    /**
     * 按住拍最大时间最大值.
     */
    private static int MAX_PRESS_TO = 180 * 1000;

    private TextView tv_shooting;

    private RelativeLayout rl_resolution;
    private RelativeLayout rl_water_marker;

    private TextView tv_video_resolution;
    private TextView et_video_rate;
    private TextView tv_water_marker_position;

    private SliderSwitch ss_mute;
    private SliderSwitch ss_beauty;
    private SliderSwitch ss_beauty_progress;
    private SliderSwitch ss_camera;
    private SliderSwitch ss_torch;
    private SliderSwitch ss_countdown;
    private SliderSwitch ss_imports;
    private SliderSwitch ss_edit;

    private SliderSwitch ss_extended_press;
    private SliderSwitch ss_short_press;

    private TextView tv_min_time_click;
    private TextView tv_max_time_click;
    private TextView tv_min_time_press;
    private TextView tv_max_time_press;

    private SeekBar skBar_min_time_click;
    private SeekBar skBar_max_time_click;
    private SeekBar skBar_min_time_press;
    private SeekBar skBar_max_time_press;

    //默认值
    //1920x1080 pixels    4000 kbps
    //1280x720 pixels     2000 kbps
    //854x480 pixels      1000 kbps
    private int mVideoRate;

    private int mMultiClipsExtendedPressMin;
    private int mMultiClipsExtendedPressMax;
    private int mMultiClipsShortPressMin;
    private int mMultiClipsShortPressMax;

    private CaptureMode mCaptureMode = CaptureMode.MULTICLIPS_EXTENDED_PRESS;
    private WaterMarker mWaterMarker = WaterMarker.WATER_MARKER_BOTTOM_LEFT;
    private VideoResolution mVideoResolution = VideoResolution.VIDEO_720x720;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_config);

        initView();
        initData();
    }

    private void initView() {
        tv_shooting = (TextView) findViewById(R.id.tv_shooting);

        rl_resolution = (RelativeLayout) findViewById(R.id.rl_resolution);
        rl_water_marker = (RelativeLayout) findViewById(R.id.rl_water_marker);

        tv_video_resolution = (TextView) findViewById(R.id.tv_video_resolution);
        et_video_rate = (EditText) findViewById(R.id.et_video_rate);
        tv_water_marker_position = (TextView) findViewById(R.id.tv_water_marker_position);

        ss_mute = (SliderSwitch) findViewById(R.id.ss_mute);
        ss_beauty = (SliderSwitch) findViewById(R.id.ss_beauty);
        ss_beauty_progress = (SliderSwitch) findViewById(R.id.ss_beauty_progress);
        ss_camera = (SliderSwitch) findViewById(R.id.ss_camera_switch);
        ss_torch = (SliderSwitch) findViewById(R.id.ss_torch);
        ss_countdown = (SliderSwitch) findViewById(R.id.ss_countdown);
        ss_imports = (SliderSwitch) findViewById(R.id.ss_imports);
        ss_edit = (SliderSwitch) findViewById(R.id.ss_edit);

        ss_extended_press = (SliderSwitch) findViewById(R.id.ss_extended_press);
        ss_short_press = (SliderSwitch) findViewById(R.id.ss_short_press);

        tv_min_time_click = (TextView) findViewById(R.id.tv_min_time_click);
        tv_max_time_click = (TextView) findViewById(R.id.tv_max_time_click);
        tv_min_time_press = (TextView) findViewById(R.id.tv_min_time_press);
        tv_max_time_press = (TextView) findViewById(R.id.tv_max_time_press);

        skBar_min_time_click = (SeekBar) findViewById(R.id.skBar_min_time_click);
        skBar_max_time_click = (SeekBar) findViewById(R.id.skBar_max_time_click);
        skBar_min_time_press = (SeekBar) findViewById(R.id.skBar_min_time_press);
        skBar_max_time_press = (SeekBar) findViewById(R.id.skBar_max_time_press);

        tv_shooting.setOnClickListener(this);
        rl_resolution.setOnClickListener(this);
        rl_water_marker.setOnClickListener(this);

        skBar_min_time_click.setOnSeekBarChangeListener(this);
        skBar_max_time_click.setOnSeekBarChangeListener(this);
        skBar_min_time_press.setOnSeekBarChangeListener(this);
        skBar_max_time_press.setOnSeekBarChangeListener(this);

        ss_beauty.setOnSwitchChangedListener(this);
        ss_extended_press.setOnSwitchChangedListener(this);
        ss_short_press.setOnSwitchChangedListener(this);
    }

    private void initData() {
        //此处精确到s, 用户可自定义
        mMultiClipsExtendedPressMin = skBar_min_time_click.getProgress() * (MIN_RELEASE_TO - MIN_RELEASE_FROM) /
                1000 /
                skBar_min_time_click.getMax() +
                MIN_RELEASE_FROM / 1000;
        tv_min_time_click.setText(mMultiClipsExtendedPressMin + "s");

        mMultiClipsExtendedPressMax = skBar_max_time_click.getProgress() * (MAX_RELEASE_TO - MAX_RELEASE_FROM) /
                1000 /
                skBar_max_time_click.getMax() +
                MAX_RELEASE_FROM / 1000;
        tv_max_time_click.setText(mMultiClipsExtendedPressMax + "s");

        mMultiClipsShortPressMin = skBar_min_time_press.getProgress() * (MIN_PRESS_TO - MIN_PRESS_FROM) /
                1000
                / skBar_min_time_press.getMax() +
                MIN_PRESS_FROM / 1000;
        tv_min_time_press.setText(mMultiClipsShortPressMin + "s");

        mMultiClipsShortPressMax = skBar_max_time_press.getProgress() * (MAX_PRESS_TO - MAX_PRESS_FROM) /
                1000 /
                skBar_max_time_press.getMax() +
                MAX_PRESS_FROM / 1000;
        tv_max_time_press.setText(mMultiClipsShortPressMax + "s");

        mVideoRate = 2000 * 1000;//720p
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shooting:
                shooting();
                break;
            case R.id.rl_resolution:
                selectResolution();
                break;
            case R.id.rl_water_marker:
                selectWaterMarkerPosition();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.skBar_min_time_click:
                mMultiClipsExtendedPressMin = progress * (MIN_RELEASE_TO - MIN_RELEASE_FROM) / 1000 / seekBar.getMax() + MIN_RELEASE_FROM / 1000;
                tv_min_time_click.setText(mMultiClipsExtendedPressMin + "s");
                break;
            case R.id.skBar_max_time_click:
                mMultiClipsExtendedPressMax = progress * (MAX_RELEASE_TO - MAX_RELEASE_FROM) / 1000 / seekBar.getMax() + MAX_RELEASE_FROM / 1000;
                tv_max_time_click.setText(mMultiClipsExtendedPressMax + "s");
                break;
            case R.id.skBar_min_time_press:
                mMultiClipsShortPressMin = progress * (MIN_PRESS_TO - MIN_PRESS_FROM) / 1000 / seekBar.getMax() + MIN_PRESS_FROM / 1000;
                tv_min_time_press.setText(mMultiClipsShortPressMin + "s");
                break;
            case R.id.skBar_max_time_press:
                mMultiClipsShortPressMax = progress * (MAX_PRESS_TO - MAX_PRESS_FROM) / 1000 / seekBar.getMax() + MAX_PRESS_FROM / 1000;
                tv_max_time_press.setText(mMultiClipsShortPressMax + "s");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onSwitchChanged(SliderSwitch obj, int status) {
        switch (obj.getId()) {
            case R.id.ss_beauty:
                ((View) ss_beauty_progress.getParent()).setEnabled(SliderSwitch.SWITCH_ON == status);
                ss_beauty_progress.setEnabled(SliderSwitch.SWITCH_ON == status);
                break;
            case R.id.ss_extended_press:
                skBar_min_time_click.setEnabled(SliderSwitch.SWITCH_ON == status);
                skBar_max_time_click.setEnabled(SliderSwitch.SWITCH_ON == status);
                break;
            case R.id.ss_short_press:
                skBar_min_time_press.setEnabled(SliderSwitch.SWITCH_ON == status);
                skBar_max_time_press.setEnabled(SliderSwitch.SWITCH_ON == status);
                break;
        }
    }

    private void shooting() {
        if (ss_extended_press.getStatus() != SliderSwitch.SWITCH_ON &&
                ss_short_press.getStatus() != SliderSwitch.SWITCH_ON) {
            Toast.makeText(CaptureConfigActivity.this, "至少选择一种拍摄模式", Toast.LENGTH_LONG).show();
            return;
        }

        if (mMultiClipsExtendedPressMin > mMultiClipsExtendedPressMax) {
            Toast.makeText(CaptureConfigActivity.this, "放开拍最小时长不能大于最大时长", Toast.LENGTH_LONG).show();
            return;
        }

        if (mMultiClipsShortPressMin > mMultiClipsShortPressMax) {
            Toast.makeText(CaptureConfigActivity.this, "长按拍最小时长不能大于最大时长", Toast.LENGTH_LONG).show();
            return;
        }

        mVideoRate = Integer.parseInt(et_video_rate.getText().toString().trim()) * 1000;
        if (mVideoRate <= 0) {
            Toast.makeText(CaptureConfigActivity.this, "视频录制码率错误", Toast.LENGTH_LONG).show();
            return;
        }

        //mCaptureMode;
        if (ss_extended_press.getStatus() == SliderSwitch.SWITCH_ON &&
                ss_short_press.getStatus() == SliderSwitch.SWITCH_ON) {
            mCaptureMode = CaptureMode.MULTICLIPS_EXTENDED_SHORT_PRESS;
        } else {
            if (ss_extended_press.getStatus() == SliderSwitch.SWITCH_ON) {
                mCaptureMode = CaptureMode.MULTICLIPS_EXTENDED_PRESS;
            } else if (ss_short_press.getStatus() == SliderSwitch.SWITCH_ON) {
                mCaptureMode = CaptureMode.MULTICLIPS_SHORT_PRESS;
            }
        }

        MuseVideoServiceImpl museVideoService = MuseVideoManager.getInstance()
                .getMuseVideoImpl();

        //可自定义值，未配置选项采取默认值
        VideoCreateInfo info = new VideoCreateInfo.Builder()
                //显示倒计时开关，默认true（显示）
                .setEnableCountDown(ss_countdown.getStatus() == SliderSwitch.SWITCH_ON)
                //显示静音开关，默认true（显示）
                .setEnableMuteSwitch(ss_mute.getStatus() == SliderSwitch.SWITCH_ON)
                //显示闪光灯开关，默认true（显示）
                .setEnableTorchSwitch(ss_torch.getStatus() == SliderSwitch.SWITCH_ON)
                //显示美颜开关，默认true（显示）
                .setEnableBeautySwitch(ss_beauty.getStatus() == SliderSwitch.SWITCH_ON)
                //显示前后摄像头切换开关，默认true（显示）
                .setEnableCameraSwitch(ss_camera.getStatus() == SliderSwitch.SWITCH_ON)
                //显示美颜幅度调节开关，默认true（显示）．若美颜关, 美颜幅度调节开关不起作用
                .setEnableBeautySetProgress(ss_beauty_progress.getStatus() == SliderSwitch.SWITCH_ON)
                //可自定义 1~100, 默认50
                .setDefaultBeautyProgress(50)
                //开启导入，默认true （开启）
                .setEnableImport(ss_imports.getStatus() == SliderSwitch.SWITCH_ON)
                //开启编辑，默认true （开启）
                .setEnableEdit(ss_edit.getStatus() == SliderSwitch.SWITCH_ON)
                //后置摄像头美颜关，默认false  （关闭）
                .setEnableBeautyBack(false)
                //前置摄像头美颜开，默认true （开启）
                .setEnableBeautyFront(true)
                //视频录制码率，默认4MB
                .setVideoRate(mVideoRate)
                //视频录制分辨率，默认720*720. 参考 VideoResolution
                .setCaptureResolution(mVideoResolution)
                //拍摄模式：长按拍&s松开拍．默认松开拍．参考CaptureMode.
                .setCaptureMode(mCaptureMode)
                //长按拍最小时长和最大时长，默认分别为3s和15s
                .setMultiClipsShortPressMax(mMultiClipsShortPressMin * 1000, mMultiClipsShortPressMax * 1000)
                //松开拍最小时长和最大时长，默认分别为3s和15s
                .setMultiClipsExtendedPressRange(mMultiClipsExtendedPressMin * 1000, mMultiClipsExtendedPressMax * 1000)
                //水印位置(左下，右下和无)．默认左下．参考WaterMarker
                .setWaterMarkPosition(mWaterMarker)
                //水印资源图片路径（路径格式：1, "assets://image.png";  2, "drawable://" + R.drawable.image）
                .setWaterMarkPath(Constant.WATER_MARK_PATH)
                //视频资源保存路径.默认"DCIM/Camera/MuseSDK"
                .setMediaStorePath(Constant.MEDIA_STORE_PATH)
                //拍摄页面打开摄像头. 默认后置摄像头
                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
                //导出视频最短时间．默认1s
                .setImportVideoMinDuration(mMultiClipsExtendedPressMin * 1000)
                // 导出视频最长时间．默认180s
                .setImportVideoMaxDuration(mMultiClipsExtendedPressMax * 1000)
                .build();

        museVideoService.initRecord(info);
        museVideoService.showRecordPage(this, CAPTURE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAPTURE_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    String outputPath = data.getStringExtra("outputPath");
                }///storage/emulated/0/DCIM/Camera
                break;
        }
    }

    private void selectResolution() {
        final CharSequence[] items = {"480 * 480", "480 * 640", "480 * 854",
                "720 * 720", "720 * 960", "720 * 1280",
                "1080 * 1080", "1080 * 1440", "1080 * 1920"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置视频分辨率").
                setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(ConfigActivity.this, items[item], Toast.LENGTH_SHORT).show();
                        tv_video_resolution.setText(items[item]);
                        if (item == 0) {
                            mVideoResolution = VideoResolution.VIDEO_480x480;
                            et_video_rate.setText("1000");
                        } else if (item == 1) {
                            mVideoResolution = VideoResolution.VIDEO_480x640;
                            et_video_rate.setText("1000");
                        } else if (item == 2) {
                            mVideoResolution = VideoResolution.VIDEO_480x854;
                            et_video_rate.setText("1000");
                        } else if (item == 3) {
                            mVideoResolution = VideoResolution.VIDEO_720x720;
                            et_video_rate.setText("2000");
                        } else if (item == 4) {
                            mVideoResolution = VideoResolution.VIDEO_720x960;
                            et_video_rate.setText("2000");
                        } else if (item == 5) {
                            mVideoResolution = VideoResolution.VIDEO_720x1280;
                            et_video_rate.setText("2000");
                        } else if (item == 6) {
                            mVideoResolution = VideoResolution.VIDEO_1080x1080;
                            et_video_rate.setText("4000");
                        } else if (item == 7) {
                            mVideoResolution = VideoResolution.VIDEO_1080x1440;
                            et_video_rate.setText("4000");
                        } else if (item == 8) {
                            mVideoResolution = VideoResolution.VIDEO_1080x1920;
                            et_video_rate.setText("4000");
                        }

                    }
                }).show();
    }

    private void selectWaterMarkerPosition() {
        final CharSequence[] items = {"无", "左下方", "右下方"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置水印位置").
                setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(ConfigActivity.this, items[item], Toast.LENGTH_SHORT).show();
                        if (item == 0) {
                            mWaterMarker = WaterMarker.WATER_MARKER_NOTHING;
                        } else if (item == 1) {
                            mWaterMarker = WaterMarker.WATER_MARKER_BOTTOM_LEFT;
                        } else if (item == 2) {
                            mWaterMarker = WaterMarker.WATER_MARKER_BOTTOM_RIGHT;
                        }

                        tv_water_marker_position.setText(items[item]);
                    }
                }).show();
    }
}
