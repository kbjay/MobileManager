package com.mobilemanager.wzq.mobilemanager.application;

import android.app.ActivityManager;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.mobilemanager.wzq.mobilemanager.R;
import com.mobilemanager.wzq.mobilemanager.Service.MyPhoneListenService;
import com.mobilemanager.wzq.mobilemanager.bean.ProcessInfo;
import com.mobilemanager.wzq.mobilemanager.util.ProcessUtils;
import com.mobilemanager.wzq.mobilemanager.widget.MyKillProWidget;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/25.
 */
public class MyApplication  extends Application {

    public static String down_url;
    public static SharedPreferences sp;
    private Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        down_url="http://192.168.3.42/MobileManagerServer";
        sp=getSharedPreferences("config",MODE_PRIVATE);
        //启动来电显示归属地服务
        intent = new Intent(this, MyPhoneListenService.class);
        startService(intent);
        //处理pendingIntent发送过来的intent,动态注册该receiver
        registerReceiver(new MyWidgetReceiver(),new IntentFilter("com.wzq.widget.broadcast"));
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(intent);
    }
    class MyWidgetReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("我收到了广播！！！！！1=-=+_=_=_=_=_+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
            //接收到广播之后，利用remoteView更新widget，
            //kill掉所有的非系统非本进程的所有进程
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ArrayList<ProcessInfo> runningProcessInfo = ProcessUtils.getRunningProcessInfo(MyApplication.this);
            for (ProcessInfo info :
                    runningProcessInfo) {
                if (!info.isSystem() && !info.getPackage_name().equals(getPackageName())) {
                    am.killBackgroundProcesses(info.getPackage_name());
                }
            }
            //重新查看进程数和ramsize给到widget
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_killpro);
            remoteViews.setTextViewText(R.id.tv_widget_ramsize,"可用内存："+ProcessUtils.getAvailableRamSize(MyApplication.this));
            remoteViews.setTextViewText(R.id.tv_widget_pronum,"进程数："+ProcessUtils.getProcessNum(MyApplication.this)+"");
//            remoteViews.setTextViewText(R.id.tv_widget_ramsize,"nihao");
//            remoteViews.setTextViewText(R.id.tv_widget_pronum,"jkljlkj");
            AppWidgetManager awm = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, MyKillProWidget.class);
            awm.updateAppWidget(componentName,remoteViews);
        }
    }
}
