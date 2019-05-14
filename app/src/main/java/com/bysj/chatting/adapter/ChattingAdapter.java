package com.bysj.chatting.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bysj.chatting.R;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.bean.ChattingBean;
import com.bysj.chatting.bean.UserBean;
import com.bysj.chatting.util.ImageUitl;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.List;

/**
 * Created by shaoxin on 19-5-3.
 * 消息列表的适配器
 */

public class ChattingAdapter extends BaseAdapter {
    private Context context;
    private List<ChattingBean> listItems;
    private LayoutInflater listContainer;
    private final String myId;

    /**
     * Item的组件对象
     */
    public final class ViewHolder {
        public QMUIRadiusImageView qivFriend;
        public QMUIRadiusImageView qivMine;
        public TextView tvFriend;
        public TextView tvMine;
        public LinearLayout llFriend;
        public LinearLayout llMine;
    }

    public ChattingAdapter(Context context, List<ChattingBean> listItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = listItems;

        ChattingApplication application = (ChattingApplication) context.getApplicationContext();
        myId = application.getMiId();
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = listContainer.inflate(R.layout.item_chatting, null);
            //获得控件对象
            vh.qivFriend = convertView.findViewById(R.id.qiv_friend_avatar);
            vh.qivMine = convertView.findViewById(R.id.qiv_my_avatar);
            vh.tvFriend = convertView.findViewById(R.id.tv_friend_msg);
            vh.tvMine = convertView.findViewById(R.id.tv_my_msg);
            vh.llFriend = convertView.findViewById(R.id.ll_friend);
            vh.llMine = convertView.findViewById(R.id.ll_mine);
            //设置空间集到convertView
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // 设置属性
        ChattingBean cb = listItems.get(position);
        if (myId.equals(cb.getReceiverId())) {
            // 我收到的
            vh.tvFriend.setText(cb.getContent());
            vh.llFriend.setVisibility(View.VISIBLE);
            vh.llMine.setVisibility(View.GONE);
            ImageUitl.showNetImage(vh.qivFriend, cb.getSenderAvatar());
            ImageUitl.showNetImage(vh.qivMine, cb.getReceiverAvatar());
        } else if (myId.equals(cb.getSenderId())) {
            // 我发出去的
            vh.tvMine.setText(cb.getContent());
            vh.llFriend.setVisibility(View.GONE);
            vh.llMine.setVisibility(View.VISIBLE);
            ImageUitl.showNetImage(vh.qivMine, cb.getSenderAvatar());
            ImageUitl.showNetImage(vh.qivFriend, cb.getReceiverAvatar());
        } else {
            // 其他情况不做处理
        }

        return convertView;
    }
}
