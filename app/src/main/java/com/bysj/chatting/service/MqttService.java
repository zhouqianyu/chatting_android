package com.bysj.chatting.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttService extends Service {
    MqttCallback callback;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void connect(String clientId, String topic){
        try {

            MqttClient client = new MqttClient(
                    "tcp://39.105.84.114:1883", clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            client.setCallback(callback);
            client.connect();
            client.subscribe(topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setCallback(MqttCallback callback){
        this.callback = callback;
    }
}
