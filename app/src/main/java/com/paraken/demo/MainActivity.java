package com.paraken.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

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
