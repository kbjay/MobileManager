package com.mobilemanager.wzq.mobilemanager.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/3/25.
 */
public class MyApplication  extends Application {

    public static String down_url;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        down_url="http://192.168.3.42/MobileManagerServer";
        sp=getSharedPreferences("config",MODE_PRIVATE);
    }
}
