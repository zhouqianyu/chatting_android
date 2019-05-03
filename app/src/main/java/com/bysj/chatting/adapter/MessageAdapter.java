package com.bysj.chatting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bysj.chatting.R;
import com.bysj.chatting.bean.MessageBean;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.List;

/**
 * Created by shaoxin on 19-5-3.
 * 消息列表的适配器
 */

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<MessageBean> listItems;
    private LayoutInflater listContainer;

    /**
     * Item的组件对象
     */
    public final class ViewHolder {
        public QMUIRadiusImageView qivAvatar;
        public TextView tvNickname;
        public TextView tvContent;
        public TextView tvTime;
        public ImageView ivRead;
    }

    public MessageAdapter(Context context, List<MessageBean> listItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = listItems;
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
            convertView = listContainer.inflate(R.layout.item_message, null);
            //获得控件对象
            vh.qivAvatar = convertView.findViewById(R.id.qiv_avatar);
            vh.tvNickname = convertView.findViewById(R.id.tv_nickname);
            vh.tvContent = convertView.findViewById(R.id.tv_content);
            vh.tvTime = convertView.findViewById(R.id.tv_time);
            vh.ivRead = convertView.findViewById(R.id.iv_read);
            //设置空间集到convertView
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        MessageBean mb = listItems.get(position);
        // TODO 设置属性

        return convertView;
    }
}
