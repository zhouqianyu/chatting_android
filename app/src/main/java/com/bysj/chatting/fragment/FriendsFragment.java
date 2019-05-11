package com.bysj.chatting.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.bysj.chatting.adapter.FriendAdapter;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.bean.UserBean;
import com.bysj.chatting.util.CallBackUtil;
import com.bysj.chatting.util.Constant;
import com.bysj.chatting.util.OkhttpUtil;
import com.bysj.chatting.view.ClearEditText;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by shaoxin on 19-5-2.
 * 好友列表
 */

public class FriendsFragment extends Fragment {
    // 定义对象
    private ClearEditText cetSearch;
    private SmartRefreshLayout smartRefreshLayout;
    private ListView lvFriend;

    // 数据列表
    private List<UserBean> listItemsRe;
    private List<UserBean> listItems;
    private FriendAdapter adapter;

    // Loading的
//    private QMUITipDialog tipDialog;

    ChattingApplication application;

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
        application = (ChattingApplication) getActivity().getApplication();
        cetSearch = getActivity().findViewById(R.id.cet_search_friend);
        lvFriend = getActivity().findViewById(R.id.lv_friend);
        smartRefreshLayout = getActivity().findViewById(R.id.smart_refresh_friend);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        // TODO 设置下拉刷新的监听器

        listItems = new ArrayList<>();
        listItemsRe = new ArrayList<>();
        adapter = new FriendAdapter(getActivity(), listItems);
        lvFriend.setAdapter(adapter);
        // 设置监听器
        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                UserBean user = listItems.get(position);
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                intent.putExtra("friendName", user.getUsername());
                intent.putExtra("friendId", user.getUuid());
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

//        tipDialog = new QMUITipDialog.Builder(getContext())
//                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
//                .setTipWord("玩命加载中")
//                .create();
    }

    /**
     * 获取列表数据并设置到页面上
     */
    private void getListContent() {
//        tipDialog.show();
        String url = Constant.BASE_DB_URL + "user/friends";
        Map<String, String> map = new HashMap<>();
        map.put("token", application.getToken());

        OkhttpUtil.okHttpPost(url, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
//                tipDialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_response_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        String data = jsonObject.getString("data");
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            JSONObject user = item.getJSONObject("user");
                            UserBean userBean = new UserBean();
                            // TODO 假数据
                            userBean.setAvatar("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=110576933,1748619052&fm=27&gp=0.jpg");
                            userBean.setUuid(user.getString("uuid"));
                            userBean.setUsername(user.getString("username"));
                            // TODO 假数据
                            userBean.setDescribe("该用户很懒，没有写头像");
                            listItemsRe.add(userBean);
                        }
                        doSearch("");
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), R.string.server_response_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
//                    tipDialog.dismiss();
                }
            }
        });

//        for (int i = 0; i < 10; i++) {
//            UserBean userBean = new UserBean();
//            userBean.setUuid(i + "1231");
//            userBean.setAvatar("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=110576933,1748619052&fm=27&gp=0.jpg");
//            userBean.setUsername("好友" + i);
//            userBean.setDescribe("该好友很懒，什么都没写");
//            listItemsRe.add(userBean);
//        }

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
