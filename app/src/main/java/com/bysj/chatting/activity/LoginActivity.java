package com.bysj.chatting.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.chatting.R;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.util.CallBackUtil;
import com.bysj.chatting.util.Constant;
import com.bysj.chatting.util.ImageUitl;
import com.bysj.chatting.util.OkhttpUtil;
import com.bysj.chatting.view.ClearEditText;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by shaoxin on 19-5-2.
 * 登录页
 */

public class LoginActivity extends AppCompatActivity {
    // 定义UI对象
    private TextView tvLogin;
    private ClearEditText etAccount;
    private ClearEditText etPassword;
    private ProgressBar progressBar;
    private QMUIRadiusImageView qivHeader;
    SharedPreferences perPreferences;
    SharedPreferences.Editor editor;
    ChattingApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        application = (ChattingApplication) getApplication();
        tvLogin = findViewById(R.id.tv_login);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.cet_password);
        progressBar = findViewById(R.id.progressBar);
        qivHeader = findViewById(R.id.qiv_header);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void initData() {
        perPreferences = getSharedPreferences("loginmsg", MODE_PRIVATE);
        editor = perPreferences.edit();
        if (perPreferences != null) {
            String mName = perPreferences.getString("name", "");
            String mPwd = perPreferences.getString("pass", "");
            String imgUrl = perPreferences.getString("img_url", "");
            etAccount.setText(mName);
            etPassword.setText(mPwd);
            ImageUitl.showNetImage(qivHeader, imgUrl);
        }
    }

    /**
     * 点击登录的方法
     */
    private void login() {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        editor.putString("name", account);
        editor.putString("pass", password);
        editor.putString("img_url", "http://www.zhouqianyu.com:8080/chatting/img/head_1aLIcbJfcdV981uuVqPWqIkFtY8B87Rp.jpg");
        editor.commit();
        if (checkFormit(account, password)) {
            doLogin(account, password);
        }
    }

    private void doLogin(String account, String password) {
        progressBar.setVisibility(View.VISIBLE);
        String url = Constant.BASE_DB_URL + "login";
        Map<String, String> map = new HashMap<>();
        map.put("username", account);
        map.put("password", password);
        OkhttpUtil.okHttpPost(url, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(LoginActivity.this, R.string.server_response_error, Toast.LENGTH_SHORT);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String token = data.getString("token");
                        String uuid = data.getString("uuid");
                        application.setToken(token);
                        // TODO 设置我的id
                        application.setMiId(uuid);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
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
