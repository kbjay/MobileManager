package com.mobilemanager.wzq.mobilemanager.activity;
//主要是自定义组合控件的实现
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.mobilemanager.wzq.mobilemanager.R;

public class SettingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }
    public void changeLocation(View v){
        startActivity(new Intent(this,SettingLocationActivity.class));
    }

}
