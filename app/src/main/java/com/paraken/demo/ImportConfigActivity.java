package com.paraken.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.paraken.demo.Util.Constant;
import com.paraken.musevideosdk.MuseVideoManager;
import com.paraken.musevideosdk.impl.MuseVideoServiceImpl;
import com.paraken.musevideosdk.session.VideoCreateInfo;
import com.paraken.musevideosdk.util.FileUtil;

public class ImportConfigActivity extends Activity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {
    private static final int IMPORT_REQUEST = 2;
    private static final int REQUEST_CODE_TAKE_VIDEO = 3;
    /**
     * 导入视频最短时间最小值.  用户可以自己设置．　此处只是展示该功能
     */
    private static int MIN_DURATION_FROM = 3 * 1000;
    /**
     * 导入视频最短时间最大值.
     */
    private static int MIN_DURATION_TO = 8 * 1000;

    /**
     * 导入视频最长时间最小值.
     */
    private static int MAX_DURATION_FROM = 15 * 1000;
    /**
     * 导入视频最长时间最大值
     */
    private static int MAX_DURATION_TO = 180 * 1000;

    private TextView tv_import;

    private TextView tv_min_time_import;
    private TextView tv_max_time_import;

    private SeekBar skBar_min_time_import;
    private SeekBar skBar_max_time_import;

    private int mImportVideoMinDuration;
    private int mImportVideoMaxDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_config);

        initView();
        initData();
    }

    private void initView() {
        tv_import = (TextView) findViewById(R.id.tv_import);

        tv_min_time_import = (TextView) findViewById(R.id.tv_min_time_import);
        tv_max_time_import = (TextView) findViewById(R.id.tv_max_time_import);

        skBar_min_time_import = (SeekBar) findViewById(R.id.skBar_min_time_import);
        skBar_max_time_import = (SeekBar) findViewById(R.id.skBar_max_time_import);

        tv_import.setOnClickListener(this);
        skBar_min_time_import.setOnSeekBarChangeListener(this);
        skBar_max_time_import.setOnSeekBarChangeListener(this);
    }

    private void initData() {
        //此处精确到s, 用户可自定义
        mImportVideoMinDuration = skBar_min_time_import.getProgress() * (MIN_DURATION_TO - MIN_DURATION_FROM) /
                1000 /
                skBar_min_time_import.getMax() +
                MIN_DURATION_FROM / 1000;
        tv_min_time_import.setText(mImportVideoMinDuration + "s");

        mImportVideoMaxDuration = skBar_max_time_import.getProgress() * (MAX_DURATION_TO - MAX_DURATION_FROM) /
                1000 /
                skBar_max_time_import.getMax() +
                MAX_DURATION_FROM / 1000;
        tv_max_time_import.setText(mImportVideoMaxDuration + "s");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_import:
                importVideo();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.skBar_min_time_import:
                mImportVideoMinDuration = progress * (MIN_DURATION_TO - MIN_DURATION_FROM) / 1000 / seekBar.getMax() + MIN_DURATION_FROM / 1000;
                tv_min_time_import.setText(mImportVideoMinDuration + "s");
                break;
            case R.id.skBar_max_time_import:
                mImportVideoMaxDuration = progress * (MAX_DURATION_TO - MAX_DURATION_FROM) / 1000 / seekBar.getMax() + MAX_DURATION_FROM / 1000;
                tv_max_time_import.setText(mImportVideoMaxDuration + "s");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private void importVideo() {
        MuseVideoServiceImpl museVideoService = MuseVideoManager.getInstance()
                .getMuseVideoImpl();
        //可自定义值，未配置选项采取默认值
        VideoCreateInfo info = new VideoCreateInfo.Builder()
                .setMediaStorePath(Constant.MEDIA_STORE_PATH)
                .setImportVideoMinDuration(mImportVideoMinDuration * 1000)
                .setImportVideoMaxDuration(mImportVideoMaxDuration * 1000)
                .setEnableEdit(false)
                .build();

        museVideoService.initRecord(info);

        try {
            Uri uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            Intent intent = new Intent(Intent.ACTION_PICK, uri);
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_TAKE_VIDEO:
                if (resultCode == RESULT_OK && data != null) {
                    processSelectVideo(data);
                }
                break;
            case IMPORT_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    String outputPath = data.getStringExtra("outputPath");
                }///storage/emulated/0/DCIM/Camera
                break;
        }
    }

    private void processSelectVideo(Intent data) {
        Uri uri = data.getData();
        if (uri != null) {
            String path = FileUtil.getPathFromUri(this, uri);
            if (!TextUtils.isEmpty(path)) {
                MuseVideoManager.getInstance()
                        .getMuseVideoImpl().showImportPage(this, path, IMPORT_REQUEST);
            }
        }
    }
}
