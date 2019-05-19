package com.bysj.chatting.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.chatting.R;
import com.bysj.chatting.activity.LoginActivity;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.util.CallBackUtil;
import com.bysj.chatting.util.Constant;
import com.bysj.chatting.util.ImageUitl;
import com.bysj.chatting.util.OkhttpUtil;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by shaoxin on 19-5-2.
 * 个人页
 */

public class MineFragment extends Fragment {
    private ChattingApplication application;
    private QMUIRadiusImageView qivAvatar;
    private TextView tvName;
    private TextView tvDescribe;
    private LinearLayout llQuit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        qivAvatar = getActivity().findViewById(R.id.qiv_mine_avatar);
        tvName = getActivity().findViewById(R.id.tv_name);
        tvDescribe = getActivity().findViewById(R.id.tv_my_describe);
        llQuit = getActivity().findViewById(R.id.ll_quit);
        llQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
    }

    private void initData() {
        application = (ChattingApplication) getActivity().getApplication();
        String avatar = application.getMyAvatar();
        ImageUitl.showNetImage(qivAvatar, avatar);

        String url = Constant.BASE_DB_URL + "user/info";
        final Map<String, String> map = new HashMap<>();
        map.put("token", application.getToken());
        OkhttpUtil.okHttpPost(url, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getActivity(), R.string.server_response_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Log.e("res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        tvDescribe.setText(data.getString("describe"));
                        tvName.setText(data.getString("username"));
                        String avatar = data.getString("img_url");
                        ImageUitl.showNetImage(qivAvatar, avatar);
                    } else {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("err", e.getLocalizedMessage());
                }
            }
        });
    }
}
