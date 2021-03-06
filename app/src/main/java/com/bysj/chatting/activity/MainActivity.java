package com.bysj.chatting.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bysj.chatting.R;
import com.bysj.chatting.adapter.MyFragmentAdapter;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.broadcast.MyReceiver;
import com.bysj.chatting.service.MqttService;
import com.bysj.chatting.util.CacheActivityUtil;
import com.bysj.chatting.util.PixelUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

/**
 * 主活动
 */
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    // 定义UI对象
    private RadioGroup rgTabBar;
    private RadioButton rbChatting;
    private RadioButton rbFriends;
    private RadioButton rbMine;
    private ViewPager vpager;
    private ChattingApplication application;

    // 定义Fragment的适配器
    private MyFragmentAdapter mAdapter;

    // 定义退出时间
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        initView();
        changeImageSize();
        rbChatting.setChecked(true);
        starMQTT();
    }


    /**
     * 挂服务
     */
    private void starMQTT() {
        MqttService mqttService = new MqttService();
        mqttService.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e("mq", "lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String str = new String(message.getPayload(), StandardCharsets.UTF_8);
                Log.e("mq", str);

                Intent intent = new Intent("wzjdgb");
                intent.putExtra("msg", str);
                sendBroadcast(intent);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.e("mq", "delivery");
            }
        });
        application.setMqttService(mqttService);
        mqttService.connect(application.getMiId(), "message_to_" + application.getMiId());
    }

    // 按键事件
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /*
    初始化，用于获取控件实例
    */
    private void initView() {
        // 透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        application = (ChattingApplication) getApplication();
        CacheActivityUtil.addActivity(MainActivity.this);
        rgTabBar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rbChatting = (RadioButton) findViewById(R.id.rb_chatting);
        rbFriends = (RadioButton) findViewById(R.id.rb_friends);
        rbMine = (RadioButton) findViewById(R.id.rb_mine);
        rgTabBar.setOnCheckedChangeListener(this);
        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_chatting:
                vpager.setCurrentItem(0);
                break;
            case R.id.rb_friends:
                vpager.setCurrentItem(1);
                break;
            case R.id.rb_mine:
                vpager.setCurrentItem(2);
                break;
        }
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case 0:
                    rbChatting.setChecked(true);
                    break;
                case 1:
                    rbFriends.setChecked(true);
                    break;
                case 2:
                    rbMine.setChecked(true);
                    break;
            }
        }
    }

    /*
    *这是设置底部图标大小的方法
    */
    private void changeImageSize() {
        final int len = PixelUtil.dip2px(MainActivity.this, 24);
        //定义底部标签图片大小
        Drawable drawable1 = getResources().getDrawable(R.drawable.menu_chatting);
        drawable1.setBounds(0, 0, len, len);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbChatting.setCompoundDrawables(null, drawable1, null, null);//只放上面
        Drawable drawable2 = getResources().getDrawable(R.drawable.menu_contact);
        drawable2.setBounds(0, 0, len, len);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbFriends.setCompoundDrawables(null, drawable2, null, null);//只放上面
        Drawable drawable3 = getResources().getDrawable(R.drawable.menu_mine);
        drawable3.setBounds(0, 0, len, len);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbMine.setCompoundDrawables(null, drawable3, null, null);//只放上面
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}