package com.mobilemanager.wzq.mobilemanager.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilemanager.wzq.mobilemanager.R;
import com.mobilemanager.wzq.mobilemanager.application.MyApplication;

public class PhoneGuardActivity extends ActionBarActivity {

    private TextView tv_phoneguard_safenumber;
    private ImageView iv_phoneguard_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_guard);
    }
    @Override
    protected void onStart() {
        //进入到这个页面，如果没有绑定sim卡则进入设置,如果开启了，那么久初始化该界面
        boolean isBindSim= MyApplication.sp.getBoolean("isBindSim", false);
        if(!isBindSim){
            startActivity(new Intent(this,PGsetup1Activity.class));
        }else{
            init();
        }
        super.onStart();
    }
    private void init() {
        tv_phoneguard_safenumber = (TextView) this.findViewById(R.id.tv_phoneguard_safenumber);
        iv_phoneguard_state = (ImageView) this.findViewById(R.id.iv_phoneguard_state);

        String safeNumber=MyApplication.sp.getString("safeNumber","");
        tv_phoneguard_safenumber.setText(safeNumber);

        //获取绑定的功能，如果没有开启则unlock
        boolean isOpenPG = MyApplication.sp.getBoolean("isOpenPG", false);
        if(isOpenPG){
            iv_phoneguard_state.setImageResource(R.drawable.lock);
        }else{
            iv_phoneguard_state.setImageResource(R.drawable.unlock);
        }
    }
    public void reset(View v){
        startActivity(new Intent(this,PGsetup1Activity.class));
    }
}
