package com.bysj.chatting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bysj.chatting.R;
import com.bysj.chatting.adapter.ChattingAdapter;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.bean.ChattingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoxin on 19-5-2.
 * 聊天页
 */

public class ChattingActivity extends AppCompatActivity {

    private String friendName;
    private int friendId;

    private TextView tvFriendName;
    private ImageView ivToAudio;
    private ImageView ivToText;
    private LinearLayout llAudioGroup;
    private LinearLayout llTextGroup;
    private ListView lvChatting;

    private List<ChattingBean> listItems;
    private ChattingAdapter adapter;
    ChattingApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattiing);
        initView();
        initData();
        setListener();
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
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        friendId = intent.getIntExtra("friendId", -1);
        friendName = intent.getStringExtra("friendName");
        tvFriendName.setText(friendName);
        ChattingBean cb = new ChattingBean();
        cb.setChattingId(1);
        cb.setSenderAvatar("http://www.zhouqianyu.com:8080/chatting/img/user.jpg");
        cb.setSenderId("test1");
        cb.setReceiverId("test");
        cb.setReceiverAvatar("http://www.fstechnology.cn:81/YZEduResources/images/0.png");
        cb.setContent("你好周千遇,我最近在研究梯度下降算法，不知道你对这块感不感兴趣，如果感兴趣的话我们可以交流一下见解");
        cb.setMyId("tes");
        listItems.add(cb);

        ChattingBean cb1 = new ChattingBean();
        cb1.setChattingId(2);
        cb1.setSenderAvatar("http://www.fstechnology.cn:81/YZEduResources/images/0.png");
        cb1.setSenderId("test");
        cb1.setReceiverId("test1");
        cb1.setReceiverAvatar("http://www.zhouqianyu.com:8080/chatting/img/user.jpg");
        cb1.setContent("你好，很高兴认识你");

        listItems.add(cb1);
        adapter.notifyDataSetChanged();
    }

    /*
    * 返回上一级
    * xml布局文件里面调用
    * */
    public void back(View view) {
        finish();
    }
}
