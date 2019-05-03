package com.bysj.chatting.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bysj.chatting.R;
import com.bysj.chatting.adapter.MessageAdapter;
import com.bysj.chatting.bean.MessageBean;
import com.bysj.chatting.view.ClearEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoxin on 19-5-2.
 * 聊天的Fragment
 */

public class ChattingFragment extends Fragment {
    // 定义对象
    private ClearEditText cetSearch;
    private SmartRefreshLayout smartRefreshLayout;
    private ListView lvMessage;

    // 数据列表
    private List<MessageBean> listItems;
    private MessageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatting, container, false);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        getListContent();
    }

    /**
     * 初始化
     */
    private void initView() {
        cetSearch = getActivity().findViewById(R.id.cet_search);
        lvMessage = getActivity().findViewById(R.id.lv_message);
        smartRefreshLayout = getActivity().findViewById(R.id.smart_refresh);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        // TODO 设置下拉刷新的监听器

        listItems = new ArrayList<>();
        adapter = new MessageAdapter(getActivity(), listItems);
        lvMessage.setAdapter(adapter);
    }

    /**
     * 获取列表数据并设置到页面上
     */
    private void getListContent() {
        for (int i = 0; i < 10; i++) {
            listItems.add(new MessageBean());
        }
        adapter.notifyDataSetChanged();
    }

}
