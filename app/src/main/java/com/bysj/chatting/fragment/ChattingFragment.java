package com.bysj.chatting.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bysj.chatting.R;
import com.bysj.chatting.activity.ChattingActivity;
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
    private List<MessageBean> listItemsRe;
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
        listItemsRe = new ArrayList<>();
        adapter = new MessageAdapter(getActivity(), listItems);
        lvMessage.setAdapter(adapter);
        cetSearch = getActivity().findViewById(R.id.cet_search);
        // 设置动作和监听器
        lvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MessageBean mb = listItems.get(position);
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                intent.putExtra("friendName", mb.getFriendName());
                intent.putExtra("friendId", mb.getFriendId());
                startActivity(intent);
            }
        });
        cetSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 执行搜索
                String key = cetSearch.getText().toString();
                doSearch(key);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * 获取列表数据并设置到页面上
     */
    private void getListContent() {
        for (int i = 0; i < 10; i++) {
            MessageBean messageBean = new MessageBean();
            int isRead = 1;
            if (i < 3) {
                isRead = 0;

            }
            messageBean.setId(i);
            messageBean.setFriendAvatar("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=110576933,1748619052&fm=27&gp=0.jpg");
            messageBean.setContent("这些消息" + i);
            messageBean.setFriendId(i + "");
            messageBean.setFriendName("好友" + i);
            messageBean.setIsDelivery(isRead);
            // TODO 时间（时分/昨天/周几/一周前）这几个梯度
            messageBean.setTime("昨天");
            listItemsRe.add(messageBean);
        }
        doSearch("");
        adapter.notifyDataSetChanged();
    }

    /**
     * 过滤搜索的方法
     */
    private void doSearch(String key) {
        listItems.clear();
        for (MessageBean messageBean : listItemsRe) {
            if (messageBean.getFriendName().contains(key) || messageBean.getContent().contains(key)) {
                listItems.add(messageBean);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
