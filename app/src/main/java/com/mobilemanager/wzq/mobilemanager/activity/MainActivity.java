package com.mobilemanager.wzq.mobilemanager.activity;
//这一页是应用的主页，包含3部分，分为，title，可以滚动的TextView，以及九宫格的实现。

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilemanager.wzq.mobilemanager.R;
import com.mobilemanager.wzq.mobilemanager.application.MyApplication;
import com.mobilemanager.wzq.mobilemanager.util.Md5Utils;

public class MainActivity extends ActionBarActivity {

    public static final int NUM_GRID_ITEMS = 9;


    private TextView tv_main_welcome;
    private GridView gv_main_content;
    private int[] mGridItemsImage;
    private String[] mGridItemsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();

        tv_main_welcome = (TextView) this.findViewById(R.id.tv_main_welcome);
        gv_main_content = (GridView) this.findViewById(R.id.gv_main_content);

        init();

    }

    public void init() {

        mGridItemsImage = new int[]{R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings
        };
        mGridItemsText = new String[]{
                "手机防盗", "通讯卫士", "软件管理",
                "进程管理", "流量控制","手机杀毒",
                 "缓存清理", "高级工具", "设置中心"};
        tv_main_welcome.setSelected(true);
        gv_main_content.setAdapter(new MyAdapter());
        gv_main_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //添加手机防盗功能1：密码设置
                        setAppPassWord();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        //设置
                        jumptoSettingPage();
                        break;
                }
            }
        });

    }


    //1：需要判断是否是第一次点击
    // 2：需要自定义dialog
    // 3：需要利用md5，将密码写入sharedprence
    public void setAppPassWord(){
        final String md5Password= MyApplication.sp.getString("md5Password","");
        if(md5Password.isEmpty()){
            //第一次
            View setPassView = View.inflate(MainActivity.this, R.layout.alert_main_setpass, null);
            final EditText et_setPass_inputpsd = (EditText) setPassView.findViewById(R.id.et_setPass_inputpsd);
            final EditText et_setPass_confirmpsd = (EditText) setPassView.findViewById(R.id.et_setPass_confirmpsd);
            Button bt_setPass_cancel = (Button) setPassView.findViewById(R.id.bt_setPass_cancel);
            Button bt_setPass_submit = (Button) setPassView.findViewById(R.id.bt_setPass_submit);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(setPassView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            bt_setPass_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            bt_setPass_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取输入信息，对比
                    String inputPass=et_setPass_inputpsd.getText().toString();
                    String confirmPass=et_setPass_confirmpsd.getText().toString();
                    if(!inputPass.isEmpty()&&!confirmPass.isEmpty()){
                        if(inputPass.equals(confirmPass)){
                            //加密，写入config
                            String password= Md5Utils.getMd5(inputPass);
                            SharedPreferences.Editor edit=MyApplication.sp.edit();
                            edit.putString("md5Password",password);
                            edit.commit();
                            alertDialog.dismiss();
                            intoPhoneGuard();

                        }else{
                            Toast.makeText(MainActivity.this, "前后输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            //第二次进来
            View inputPassView = View.inflate(MainActivity.this, R.layout.alert_main_inputpass, null);
            final EditText et_inputPass_inputpsd = (EditText) inputPassView.findViewById(R.id.et_inputPass_inputpsd);
            Button bt_inputPass_cancel = (Button) inputPassView.findViewById(R.id.bt_inputPass_cancel);
            Button bt_inputPass_submit = (Button) inputPassView.findViewById(R.id.bt_inputPass_submit);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(inputPassView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            bt_inputPass_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            bt_inputPass_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String et_pass = et_inputPass_inputpsd.getText().toString();
                    if(!et_pass.isEmpty()){
                        if(md5Password.equals(Md5Utils.getMd5(et_pass))){
                            intoPhoneGuard();
                            alertDialog.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void intoPhoneGuard(){
        Intent intent = new Intent(this,PhoneGuardActivity.class);
        startActivity(intent);
    }
    public void jumptoSettingPage(){
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return NUM_GRID_ITEMS;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout ll = (LinearLayout) View.inflate(MainActivity.this, R.layout.item_main_grid, null);

            ImageView iv_mian_griditem = (ImageView) ll.findViewById(R.id.iv_mian_griditem);
            TextView tv_main_griditem = (TextView) ll.findViewById(R.id.tv_main_griditem);
            iv_mian_griditem.setImageResource(mGridItemsImage[position]);
            tv_main_griditem.setText(mGridItemsText[position]);
            return ll;
        }
    }
}
