package com.example.qingyun.myfirstapp.utils;

import android.app.Activity;

import com.example.qingyun.myfirstapp.ChatMainActivity;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CacheMessage {
    static public HashMap<String, ChatMainActivity> observerMap = new HashMap<>();
    static public ChatMsgRecord message = null;

    public static ChatMsgRecord getMessage() {
        return message;
    }

    public static void setMessage(ChatMsgRecord message) {
        CacheMessage.message = message;
        Set<Map.Entry<String,ChatMainActivity>> set = observerMap.entrySet();
        for (Map.Entry entry : set){
            ChatMainActivity chatMainActivity  = (ChatMainActivity) entry.getValue();
            chatMainActivity.setMessage(message);
        }
    }

}
