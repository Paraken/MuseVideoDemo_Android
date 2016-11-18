package com.paraken.demo;

import android.app.Application;
import android.content.Context;

import com.paraken.demo.Util.Constant;
import com.paraken.musevideosdk.MuseVideoManager;
import com.paraken.musevideosdk.impl.MuseVideoServiceImpl;

/**
 * Created by lifs on 16-09-08.
 */
public class SDKApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initSDK();
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void initSDK() {
        MuseVideoManager.getInstance().init(this);
        MuseVideoServiceImpl museVideoService = MuseVideoManager.getInstance()
                .getMuseVideoImpl();
        museVideoService.register(Constant.APP_KEY, Constant.APP_CODE, Constant.APP_SECRET);
    }

}
