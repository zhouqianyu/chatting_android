package com.bysj.chatting.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.chatting.R;
import com.bysj.chatting.adapter.ChattingAdapter;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.bean.ChattingBean;
import com.bysj.chatting.broadcast.MyReceiver;
import com.bysj.chatting.service.MqttService;
import com.bysj.chatting.util.CallBackUtil;
import com.bysj.chatting.util.Constant;
import com.bysj.chatting.util.OkhttpUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by shaoxin on 19-5-2.
 * 聊天页
 */

public class ChattingActivity extends AppCompatActivity {

    private String friendName;
    private String friendId;
    private String friendAvatar;

    private TextView tvFriendName;
    private ImageView ivToAudio;
    private ImageView ivToText;
    private LinearLayout llAudioGroup;
    private LinearLayout llTextGroup;
    private ListView lvChatting;

    private List<ChattingBean> listItems;
    private ChattingAdapter adapter;
    ChattingApplication application;
    // 广播
    MyReceiver receiver;
    IntentFilter filter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattiing);
        initView();
        initData();
        setListener();
        starMQTT();
        initReceiver();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        application = (ChattingApplication) getApplication();
        tvFriendName = findViewById(R.id.tv_friend_name);
        ivToAudio = findViewById(R.id.iv_to_audio);
        ivToText = findViewById(R.id.iv_to_text);
        llAudioGroup = findViewById(R.id.ll_audio_group);
        llTextGroup = findViewById(R.id.ll_text_group);
        lvChatting = findViewById(R.id.lv_chatting);

        listItems = new ArrayList<>();
        adapter = new ChattingAdapter(ChattingActivity.this, listItems);
        lvChatting.setAdapter(adapter);
    }


    /**
     * 广播相关
     */
    private void initReceiver() {
        filter = new IntentFilter("wzjdgb");
        receiver = new MyReceiver(new MyReceiver.BoardCastCallback() {
            @Override
            public void callback(String msg) {
                // TODO 修改内容
                Toast.makeText(ChattingActivity.this, "Chatting" + msg, Toast.LENGTH_SHORT).show();
            }
        });
        registerReceiver(receiver, filter);
    }

    /**
     * 设置动作和监听器
     */
    private void setListener() {
        ivToAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llTextGroup.setVisibility(View.GONE);
                llAudioGroup.setVisibility(View.VISIBLE);
            }
        });
        ivToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAudioGroup.setVisibility(View.GONE);
                llTextGroup.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 挂服务
     */
    private void starMQTT() {
        MqttService mqttService;
        if (application.getMqttService() != null) {
            mqttService = application.getMqttService();
        } else {
            mqttService = new MqttService();
            mqttService.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.e("main_mq", "lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.e("main_mq", new String(message.getPayload(), StandardCharsets.UTF_8));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.e("main_mq", "delivery");
                }
            });
            application.setMqttService(mqttService);
            mqttService.connect(application.getMiId(), "message_to_" + application.getMiId());
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        friendId = intent.getStringExtra("friendId");
        friendName = intent.getStringExtra("friendName");
        friendAvatar = intent.getStringExtra("friendAvatar");
        tvFriendName.setText(friendName);

        String url = Constant.BASE_DB_URL + "message/getMessage";
        Map<String, String> map = new HashMap<>();
        map.put("token", application.getToken());
        map.put("uuid", friendId);
        OkhttpUtil.okHttpPost(url, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(ChattingActivity.this, R.string.server_response_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Log.e("res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            ChattingBean cb = new ChattingBean();
                            cb.setChattingId(item.getInt("id"));
                            cb.setSenderId(item.getString("uuid_from"));
                            cb.setReceiverId(item.getString("uuid_to"));
                            if (application.getMiId().equals(cb.getReceiverId())) {
                                cb.setSenderAvatar(friendAvatar);
                                cb.setReceiverAvatar("http://www.fstechnology.cn:81/YZEduResources/images/0.png");
                            } else {
                                cb.setSenderAvatar("http://www.fstechnology.cn:81/YZEduResources/images/0.png");
                                cb.setReceiverAvatar(friendAvatar);
                            }
                            cb.setContent(item.getString("message"));
                            cb.setMyId(application.getMiId());
                            listItems.add(cb);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(ChattingActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
    }

    /*
    * 返回上一级
    * xml布局文件里面调用
    * */
    public void back(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
