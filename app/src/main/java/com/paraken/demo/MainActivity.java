package com.paraken.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    /**
        UI 自定义
        1, 颜色(修改colors.xml默认值);
        2, 大小(修改dimens.xml默认值);
        3, 资源图片(如下两方式皆可).
            3.1 打开.aar目录./res/drawable-x/ 替换相关图片;
            3.2 当前项目下创建同名资源文件以覆盖aar资源文件,
                demo 中提供了不同于aar一套主题，相关文件说明情况如下:
                3.2.1  *_169_*.png为16:9拍摄模式图片资源;
                3.2.2  *_11_43_*.png为1:1和4:3拍摄模式图片资源.
    */

    private Button btn_capture, btn_import;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_import = (Button) findViewById(R.id.btn_import);
        btn_capture = (Button) findViewById(R.id.btn_capture);

        btn_import.setOnClickListener(this);
        btn_capture.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_capture:
                startActivity(new Intent(this, CaptureConfigActivity.class));
                break;
            case R.id.btn_import:
                startActivity(new Intent(this, ImportConfigActivity.class));
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.exit(0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
