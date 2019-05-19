package com.bysj.chatting.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.chatting.R;
import com.bysj.chatting.adapter.ChattingAdapter;
import com.bysj.chatting.application.ChattingApplication;
import com.bysj.chatting.bean.ChattingBean;
import com.bysj.chatting.broadcast.MyReceiver;
import com.bysj.chatting.service.MqttService;
import com.bysj.chatting.util.AudioFileFunc;
import com.bysj.chatting.util.AudioRecordFunc;
import com.bysj.chatting.util.CallBackUtil;
import com.bysj.chatting.util.Constant;
import com.bysj.chatting.util.OkhttpUtil;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by shaoxin on 19-5-2.
 * 聊天页
 */

public class ChattingActivity extends AppCompatActivity {

    private String friendName;
    private String friendId;
    private String friendAvatar;

    private TextView tvFriendName;
    private ImageView ivToAudio;
    private ImageView ivToText;
    private LinearLayout llAudioGroup;
    private LinearLayout llTextGroup;
    private ListView lvChatting;

    private EditText etInput;
    private QMUIRoundButton bnSend;
    private QMUIRoundButton bnRecord;

    private List<ChattingBean> listItems;
    private ChattingAdapter adapter;
    ChattingApplication application;
    // 广播
    MyReceiver receiver;
    IntentFilter filter;

    private int MY_PERMISSIONS_REQUEST_CALL_PHONE;
    private AudioRecordFunc func;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattiing);
        initView();
        initData();
        setListener();
        initReceiver();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        application = (ChattingApplication) getApplication();
        tvFriendName = findViewById(R.id.tv_friend_name);
        ivToAudio = findViewById(R.id.iv_to_audio);
        ivToText = findViewById(R.id.iv_to_text);
        llAudioGroup = findViewById(R.id.ll_audio_group);
        llTextGroup = findViewById(R.id.ll_text_group);
        lvChatting = findViewById(R.id.lv_chatting);
        etInput = findViewById(R.id.et_input);
        bnSend = findViewById(R.id.bn_send);
        bnRecord = findViewById(R.id.bn_record);
        listItems = new ArrayList<>();
        adapter = new ChattingAdapter(ChattingActivity.this, listItems);
        lvChatting.setAdapter(adapter);
        func = AudioRecordFunc.getInstance();

        bnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }
        });
    }


    /**
     * 广播相关
     */
    private void initReceiver() {
        filter = new IntentFilter("wzjdgb");
        receiver = new MyReceiver(new MyReceiver.BoardCastCallback() {
            @Override
            public void callback(String msg) {
                try {
                    JSONObject jsonObject1 = new JSONObject(msg);
                    JSONObject jsonObject = jsonObject1.getJSONObject("log");
                    String uuidFrom = jsonObject.getString("uuid_from");
                    if (friendId.equals(uuidFrom)) {
                        ChattingBean cb = new ChattingBean();
                        cb.setChattingId(jsonObject.getInt("id"));
                        cb.setContent(jsonObject.getString("message"));
                        cb.setSenderId(uuidFrom);
                        cb.setReceiverId(application.getMiId());
                        cb.setSenderAvatar(friendAvatar);
                        cb.setReceiverAvatar(application.getMyAvatar());
                        listItems.add(cb);
                        adapter.notifyDataSetChanged();
                        // TODO 发送通知

                        String url = Constant.BASE_DB_URL + "message/getMessage";
                        Map<String, String> map = new HashMap<>();
                        map.put("token", application.getToken());
                        map.put("uuid", friendId);
                        OkhttpUtil.okHttpPost(url, map, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error", e.getLocalizedMessage());
                }
            }
        });
        registerReceiver(receiver, filter);
    }

    /**
     * 设置动作和监听器
     */
    private void setListener() {
        ivToAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llTextGroup.setVisibility(View.GONE);
                llAudioGroup.setVisibility(View.VISIBLE);
            }
        });
        ivToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAudioGroup.setVisibility(View.GONE);
                llTextGroup.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        friendId = intent.getStringExtra("friendId");
        friendName = intent.getStringExtra("friendName");
        friendAvatar = intent.getStringExtra("friendAvatar");
        tvFriendName.setText(friendName);

        String url = Constant.BASE_DB_URL + "message/getMessage";
        Map<String, String> map = new HashMap<>();
        map.put("token", application.getToken());
        map.put("uuid", friendId);
        OkhttpUtil.okHttpPost(url, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(ChattingActivity.this, R.string.server_response_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        Log.e("array", array.toString());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            ChattingBean cb = new ChattingBean();
                            cb.setChattingId(item.getInt("id"));
                            cb.setSenderId(item.getString("uuid_from"));
                            cb.setReceiverId(item.getString("uuid_to"));
                            if (application.getMiId().equals(cb.getReceiverId())) {
                                cb.setSenderAvatar(friendAvatar);
                                cb.setReceiverAvatar(application.getMyAvatar());
                            } else {
                                cb.setSenderAvatar(application.getMyAvatar());
                                cb.setReceiverAvatar(friendAvatar);
                            }

                            cb.setContent(item.getString("message"));
                            cb.setMyId(application.getMiId());
                            listItems.add(cb);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(ChattingActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 发送消息
     */
    private void sendMsg() {
        bnSend.setClickable(false);
        final String str = etInput.getText().toString();
        if ("".equals(str)) {
            Toast.makeText(ChattingActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            String url = Constant.BASE_DB_URL + "message/send";
            Map<String, String> map = new HashMap<>();
            map.put("token", application.getToken());
            map.put("toUuid", friendId);
            map.put("message", str);
            OkhttpUtil.okHttpPost(url, map, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                    Toast.makeText(ChattingActivity.this, R.string.server_response_error, Toast.LENGTH_SHORT).show();
                    bnSend.setClickable(true);
                }

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("code") == 200) {
                            ChattingBean cb = new ChattingBean();
                            cb.setChattingId(12);
                            cb.setContent(str);
                            cb.setSenderId(application.getMiId());
                            cb.setReceiverId(friendId);
                            cb.setSenderAvatar(application.getMyAvatar());
                            cb.setReceiverAvatar(friendAvatar);
                            listItems.add(cb);
                            adapter.notifyDataSetChanged();
                            etInput.setText("");
                            hideKeyBoard();
                        } else {
                            Toast.makeText(ChattingActivity.this, R.string.server_response_error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bnSend.setClickable(true);
                }
            });
        }
    }

    /*
    * 返回上一级
    * xml布局文件里面调用
    * */
    public void back(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void hideKeyBoard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        //按下操作
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startRecord();
        }
        //抬起操作
        if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
            stopRecord();
        }
        //移动操作
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

        }
        return super.onTouchEvent(motionEvent);
    }

    /**
     * 按下
     */
    private void startRecord() {
        Log.e("event", "start");
        bnRecord.setText("我在听...");
        if (ContextCompat.checkSelfPermission(ChattingActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(ChattingActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(ChattingActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChattingActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            int i = func.startRecordAndFile();
            Log.e("is", i + "");
        }
    }

    /**
     * 松开
     */
    private void stopRecord() {
        Log.e("event", "end");
        bnRecord.setText("识别中...");
        func.stopRecordAndFile();
        String path = AudioFileFunc.getWavFilePath();
        long size = func.getRecordFileSize();
        if (size > 70000) {
            upload(path);
        } else {
            bnRecord.setText("按住 说话");
            Toast.makeText(ChattingActivity.this, "录音时间过短", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ChattingActivity.this, "recoarding", Toast.LENGTH_SHORT).show();
                int i = func.startRecordAndFile();
                Log.e("is", i + "");
            } else {
                // Permission Denied
                Toast.makeText(ChattingActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 上传文件
     */
    private void upload(String path) {
        File file = new File(path);
        String url = Constant.BASE_DB_URL + "message/recognize";
        Map<String, String> map = new HashMap<>();
        map.put("token", application.getToken());
        OkhttpUtil.okHttpUploadFile(url, file, "file", "file", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(ChattingActivity.this, R.string.server_response_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Log.e("res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        String data = jsonObject.getString("data");
                        recognizeSuccess(data);
                    } else {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(ChattingActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("err", e.getLocalizedMessage());
                } finally {
                    bnRecord.setText("按住 说话");
                }
            }
        });
    }

    private void recognizeSuccess(String str) {
        llAudioGroup.setVisibility(View.GONE);
        llTextGroup.setVisibility(View.VISIBLE);
        etInput.setText("");
        etInput.setText(str);
    }
}
