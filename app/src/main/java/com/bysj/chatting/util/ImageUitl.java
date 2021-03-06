package com.bysj.chatting.util;

import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import okhttp3.Call;
import okhttp3.Response;

public class ImageUitl {
    private static String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    /**
     * 显示图片缓存的工具类
     * 先查看本地缓存，如果没有再请求网络
     *
     * @param iv  要显示那个组件
     * @param url 图片地址
     **/
    public static void showNetImage(final ImageView iv, final String url) {
        Bitmap bm = LruCacheUtils.getInstance().get(url);
        if (bm == null) {
            OkhttpUtil.okHttpGetBitmap(url, new CallBackUtil.CallBackBitmap() {
                @Override
                public void onFailure(Call call, Exception e) {

                }

                @Override
                public void onResponse(Bitmap bm) {
                    LruCacheUtils.getInstance().addBitmapToMemoryCache(url, bm);
                    // 加载完成后再从缓存池读取，不直接读取网络请求图片
                    showCacheImage(iv, url);
                }
            });
        } else {
            iv.setImageBitmap(bm);
        }
    }

    /**
     * 显示缓存图片的方法
     * @param iv  要显示那个组件
     * @param url 图片地址
     */
    public static void showCacheImage(final ImageView iv, final String url) {
        Bitmap bm = LruCacheUtils.getInstance().get(url);
        if (bm != null) {
            iv.setImageBitmap(bm);
        }
    }


    /**
     * 设置背景图片缓存的工具类
     * 先查看本地缓存，如果没有再请求网络
     *
     * @param re  要显示那个组件
     * @param url 图片地址
     **/
    public static void setBackground(final RelativeLayout re, final String url) {
        Bitmap bm = LruCacheUtils.getInstance().get(url);
        if (bm == null) {
            OkhttpUtil.okHttpGetBitmap(url, new CallBackUtil.CallBackBitmap() {
                @Override
                public void onFailure(Call call, Exception e) {

                }

                @Override
                public void onResponse(Bitmap bm) {
                    LruCacheUtils.getInstance().addBitmapToMemoryCache(url, bm);
                    Drawable drawable = new BitmapDrawable(bm);
                    re.setBackground(drawable);
                }
            });
        } else {
            Drawable drawable = new BitmapDrawable(bm);
            re.setBackground(drawable);
        }
    }

}
