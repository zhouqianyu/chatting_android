package com.bysj.chatting.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bysj.chatting.R;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.util.ImageUitl;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

/**
 * Created by shaoxin on 19-5-2.
 * 个人页
 */

public class MineFragment extends Fragment {
    private ChattingApplication application;
    private QMUIRadiusImageView qivAvatar;
    private TextView tvName;
    private TextView tvDescribe;

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
        tvDescribe = getActivity().findViewById(R.id.tv_describe);
    }

    private void initData() {
        application = (ChattingApplication) getActivity().getApplication();
        String avatar = application.getMyAvatar();
        ImageUitl.showNetImage(qivAvatar, avatar);
    }
}
