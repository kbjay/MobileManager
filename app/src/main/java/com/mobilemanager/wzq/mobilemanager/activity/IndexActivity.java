package com.mobilemanager.wzq.mobilemanager.activity;
//实现版本更新。
// 1：获取当前版本，
// 2：获取服务端版本（解析json），
// 3：比较之后，提示用户时候更新，if选择更新，下载apk调用系统instalsser更新应用即可

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.mobilemanager.wzq.mobilemanager.R;
import com.mobilemanager.wzq.mobilemanager.util.HttpUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

public class IndexActivity extends ActionBarActivity {
    public static final int JSON_MES_OK = 1;
    private float mCurrentVersion;

    //从服务器拿到的更新版本的数据
    public String mDownPath;
    public String mChangeInfo;
    public float mLatestVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentVersion = getCurrentVersion();
        downJsonData();

    }
    //33333333333333333333333333  获取json数据，并处理。
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JSON_MES_OK:
                    HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
                    //获取最新的版本号，比较，if需要更新就更新
                    mLatestVersion = Float.parseFloat(hashMap.get("latestVersionName"));
                    mChangeInfo=hashMap.get("changeInfo");
                    mDownPath=hashMap.get("downUrl");
                    if (mLatestVersion > mCurrentVersion) {
                        alert_update();
                    }
                    break;
            }

        }
    };
    //4444444444444444444444444通知用户更新版本
    public void alert_update() {
        new AlertDialog.Builder(this)
                .setTitle("更新通知")
                .setMessage(mChangeInfo)
                .setPositiveButton("确认更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downNewVersion();
                    }
                })
                .setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        //开启系统的installer程序
    }
    //55555555555555555555555555555用户选择更新之后，下载数据，下载成功提示安装，失败toast用户
    public void downNewVersion(){
        //根据mDownPath下载最新的apk
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(mDownPath, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileManager.apk");
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(responseBody);
                    fos.close();
                    //通知用户安装完成
                    new AlertDialog.Builder(IndexActivity.this)
                            .setTitle("下载完成")
                            .setTitle("是否安装")
                            .setPositiveButton("确认安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    installNewVersion(file);
                                }
                            })
                            .setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                   /* Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    startActivity(intent);*/
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public void installNewVersion(File file){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
//soutlog
    }
    //1111111111111111111111111111获取当前的版本号
    public float getCurrentVersion() {
        float currentVersion = -1;
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            currentVersion = Float.parseFloat(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersion;
    }
    //2222222222222222222222222  new线程下载json数据
    public void downJsonData() {
        //下载json数据，解析数据，获取相关的信息(需要new一个线程)！！！！！！！！
        // sxsxsx，线程结束的时候json解析，并把数据传给主线程。
        new Thread() {
            @Override
            public void run() {
                super.run();
                String path = "http://192.168.3.34/MobileMamagerServer/versionInfo.txt";
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = conn.getInputStream();
                        //根据流获取对应的String
                        String jsonString = HttpUtils.getStringFromStream(is);
                        System.out.println(jsonString + "=============================");

                        JSONObject jsonObject = new JSONObject(jsonString);
                        String latestVersionName = jsonObject.getString("versionName");
                        String changeInfo = jsonObject.getString("changeInfo");
                        String downUrl = jsonObject.getString("downUrl");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("latestVersionName", latestVersionName);
                        map.put("changeInfo", changeInfo);
                        map.put("downUrl", downUrl);

                        Message message = myHandler.obtainMessage();
                        message.what = JSON_MES_OK;
                        message.obj = map;
                        myHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

}
