package com.bysj.chatting.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bysj.chatting.R;
import com.bysj.chatting.activity.ChattingActivity;
import com.bysj.chatting.activity.MainActivity;
import com.bysj.chatting.adapter.MessageAdapter;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.bean.MessageBean;
import com.bysj.chatting.broadcast.MyReceiver;
import com.bysj.chatting.util.CallBackUtil;
import com.bysj.chatting.util.Constant;
import com.bysj.chatting.util.OkhttpUtil;
import com.bysj.chatting.view.ClearEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

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

    ChattingApplication application;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // 广播
    MyReceiver receiver;
    IntentFilter filter;

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
        initReceiver();
    }

    /**
     * 初始化
     */
    private void initView() {
        application = (ChattingApplication) getActivity().getApplication();
        cetSearch = getActivity().findViewById(R.id.cet_search);
        lvMessage = getActivity().findViewById(R.id.lv_message);
        smartRefreshLayout = getActivity().findViewById(R.id.smart_refresh);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        // 设置下拉刷新的监听器
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                listItemsRe.clear();
                listItems.clear();
                adapter.notifyDataSetChanged();
                getListContent();
                refreshlayout.finishRefresh();
            }
        });

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
                intent.putExtra("friendAvatar", mb.getFriendAvatar());
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
        String url = Constant.BASE_DB_URL + "message/friends";
        Map<String, String> map = new HashMap<>();
        map.put("token", application.getToken());
        OkhttpUtil.okHttpPost(url, map, new CallBackUtil.CallBackString() {

            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getActivity(), R.string.server_response_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        listItemsRe.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
//                            Log.e("item", item.toString());
                            MessageBean messageBean = new MessageBean();
                            messageBean.setId(item.getInt("id"));
                            messageBean.setFriendAvatar(item.getString("img_url"));
                            messageBean.setContent(item.getString("message"));
                            messageBean.setFriendId(item.getString("friend_uuid"));
                            messageBean.setFriendName(item.getString("username"));
                            messageBean.setIsDelivery(item.getInt("is_delivery"));
                            // TODO 时间（时分/昨天/周几/一周前）这几个梯度
                            if (item.getString("created_at") != null) {
                                Date date = new Date(item.getLong("created_at"));
                                messageBean.setTime(sdf.format(date));
                            }
                            listItemsRe.add(messageBean);
                        }
                        doSearch("");
                    } else {
                        Toast.makeText(getActivity(), R.string.server_response_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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


    /**
     * 广播相关
     */
    private void initReceiver() {
        filter = new IntentFilter("wzjdgb");
        receiver = new MyReceiver(new MyReceiver.BoardCastCallback() {
            @Override
            public void callback(String msg) {
                // TODO 修改内容
//                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                getListContent();
            }
        });
        getActivity().registerReceiver(receiver, filter);
    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        getListContent();
        super.onStart();
    }
}
