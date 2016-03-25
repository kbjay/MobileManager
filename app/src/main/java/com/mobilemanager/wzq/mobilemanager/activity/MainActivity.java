package com.mobilemanager.wzq.mobilemanager.activity;
//这一页是应用的主页，包含3部分，分为，title，可以滚动的TextView，以及九宫格的实现。

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilemanager.wzq.mobilemanager.R;

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
