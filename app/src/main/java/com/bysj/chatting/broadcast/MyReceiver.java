package com.bysj.chatting.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    BoardCastCallback castCallback;

    public MyReceiver(BoardCastCallback castCallback) {
        this.castCallback = castCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String msg = intent.getStringExtra("msg");
        castCallback.callback(msg);
    }

    public interface BoardCastCallback {
        void callback(String msg);
    }

    ;
}
