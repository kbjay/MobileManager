package com.mobilemanager.wzq.mobilemanager.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobilemanager.wzq.mobilemanager.R;
import com.mobilemanager.wzq.mobilemanager.application.MyApplication;


/**
 * Created by Administrator on 2016/3/25.
 */
public class MySettingView extends LinearLayout implements View.OnClickListener{

    private TextView tv_setting_choice;
    private TextView tv_setting_state;
    private CheckBox cb_setting_changestate;
    private boolean defaultState;
    private String choice;
    private String chooseState;
    private String unchooseState;
    private SharedPreferences.Editor edit;
    private String keyInConfig;


    public MySettingView(Context context) {
        super(context);
        init(null);
    }
    public MySettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        //获取处理config文件的editor
        edit = MyApplication.sp.edit();


        init(attrs);
    }
    public void init (AttributeSet attrs){

        RelativeLayout inflate = (RelativeLayout) View.inflate(getContext(), R.layout.item_setting, null);
        tv_setting_choice = (TextView) inflate.findViewById(R.id.tv_setting_choice);
        tv_setting_state = (TextView) inflate.findViewById(R.id.tv_setting_state);
        cb_setting_changestate = (CheckBox) inflate.findViewById(R.id.cb_setting_changestate);
        choice = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "choice");
        chooseState = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "chooseState");
        unchooseState = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "unchooseState");
        keyInConfig = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "keyInConfig");
        //是否自动更新由config文件决定
        defaultState = MyApplication.sp.getBoolean("autoupdate",true);

        tv_setting_choice.setText(choice);
        if(defaultState){
            tv_setting_state.setText(chooseState);
            cb_setting_changestate.setChecked(defaultState);
        }else{
            tv_setting_state.setText(unchooseState);
            cb_setting_changestate.setChecked(defaultState);
        }
        addView(inflate);
    }

    @Override
    public void onClick(View v) {
        //Q 怎么获取点击之前的状态，只需要获得checkbox的状态即可
       boolean checked = cb_setting_changestate.isChecked();
       if(checked){
           tv_setting_state.setText(unchooseState);
           cb_setting_changestate.setChecked(false);
           edit.putBoolean(keyInConfig, false);
           edit.commit();
       }else{
           tv_setting_state.setText(chooseState);
           cb_setting_changestate.setChecked(true);
           edit.putBoolean(keyInConfig,true);
           edit.commit();
       }
    }
}
