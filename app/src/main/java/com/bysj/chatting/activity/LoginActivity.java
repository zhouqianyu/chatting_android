package com.bysj.chatting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.chatting.R;


/**
 * Created by shaoxin on 19-5-2.
 * 登录页
 */

public class LoginActivity extends AppCompatActivity {
    // 定义UI对象
    private TextView tvLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tvLogin = findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    /**
     * 点击登录的方法
     */
    private void login() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*
    * 验证输入格式合法的方法
    * */
    private boolean checkFormit(String account, String password) {
        if (account.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, R.string.account_password_not_null, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            // TODO 其他输入格式判断
            return true;
        }
    }

    /*
    * 返回上一级
    * xml布局文件里面调用
    * */
    public void back(View view) {
        finish();
    }
}
