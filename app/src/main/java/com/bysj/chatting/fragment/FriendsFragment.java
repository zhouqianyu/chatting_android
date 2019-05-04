package com.bysj.chatting.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bysj.chatting.R;
import com.bysj.chatting.adapter.FriendAdapter;
import com.bysj.chatting.bean.UserBean;
import com.bysj.chatting.view.ClearEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoxin on 19-5-2.
 * 好友列表
 */

public class FriendsFragment extends Fragment {
    // 定义对象
    private ClearEditText cetSearch;
    private SmartRefreshLayout smartRefreshLayout;
    private ListView lvMessage;

    // 数据列表
    private List<UserBean> listItemsRe;
    private List<UserBean> listItems;
    private FriendAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
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
        cetSearch = getActivity().findViewById(R.id.cet_search_friend);
        lvMessage = getActivity().findViewById(R.id.lv_friend);
        smartRefreshLayout = getActivity().findViewById(R.id.smart_refresh_friend);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        // TODO 设置下拉刷新的监听器

        listItems = new ArrayList<>();
        listItemsRe = new ArrayList<>();
        adapter = new FriendAdapter(getActivity(), listItems);
        lvMessage.setAdapter(adapter);
        // 搜索
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
            UserBean userBean = new UserBean();
            userBean.setUuid(i + "1231");
            userBean.setAvatar("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=110576933,1748619052&fm=27&gp=0.jpg");
            userBean.setUsername("好友" + i);
            userBean.setDescribe("该好友很懒，什么都没写");
            listItemsRe.add(userBean);
        }
        doSearch("");
        adapter.notifyDataSetChanged();
    }

    /**
     * 过滤搜索的方法
     */
    private void doSearch(String key) {
        listItems.clear();
        for (UserBean friend : listItemsRe) {
            if (friend.getUsername().contains(key) || friend.getUuid().contains(key)) {
                listItems.add(friend);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
