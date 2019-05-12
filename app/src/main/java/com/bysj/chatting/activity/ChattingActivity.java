package com.bysj.chatting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bysj.chatting.R;

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
        tvFriendName = findViewById(R.id.tv_friend_name);
        ivToAudio = findViewById(R.id.iv_to_audio);
        ivToText = findViewById(R.id.iv_to_text);
        llAudioGroup = findViewById(R.id.ll_audio_group);
        llTextGroup = findViewById(R.id.ll_text_group);
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
    }

    /*
    * 返回上一级
    * xml布局文件里面调用
    * */
    public void back(View view) {
        finish();
    }
}
