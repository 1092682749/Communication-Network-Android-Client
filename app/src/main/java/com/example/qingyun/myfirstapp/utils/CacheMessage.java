package com.example.qingyun.myfirstapp.utils;

import android.app.Activity;

import com.example.qingyun.myfirstapp.ChatMainActivity;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;
import com.example.qingyun.myfirstapp.pojo.Observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CacheMessage {
    static public HashMap<String, Observer> observerMap = new HashMap<>();
    static public ChatMsgRecord message = null;

    public static ChatMsgRecord getMessage() {
        return message;
    }

    public static void setMessage(ChatMsgRecord message) {
        CacheMessage.message = message;
        // 通知所有观察者
        Set<Map.Entry<String,Observer>> set = observerMap.entrySet();
        for (Map.Entry entry : set){
            Observer observer  = (Observer) entry.getValue();
            // 观察者响应
            observer.setMessage(message);
        }
    }

}
