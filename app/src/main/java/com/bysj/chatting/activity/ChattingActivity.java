package com.bysj.chatting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattiing);
        initView();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tvFriendName = findViewById(R.id.tv_friend_name);
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
