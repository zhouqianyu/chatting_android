package com.bysj.chatting.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.bysj.chatting.fragment.ChattingFragment;
import com.bysj.chatting.fragment.FriendsFragment;
import com.bysj.chatting.fragment.MineFragment;

/**
 * Created by shaoxin on 18-3-25.
 * ViewPagerFragment的适配器类
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;
    private ChattingFragment myFragment0 = null;
    private FriendsFragment myFragment1 = null;
    private MineFragment myFragment2 = null;

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
        myFragment0 = new ChattingFragment();
        myFragment1 = new FriendsFragment();
        myFragment2 = new MineFragment();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = myFragment0;
                break;
            case 1:
                fragment = myFragment1;
                break;
            case 2:
                fragment = myFragment2;
                break;
        }
        return fragment;
    }

}