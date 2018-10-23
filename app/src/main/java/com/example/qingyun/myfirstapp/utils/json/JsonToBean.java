package com.example.qingyun.myfirstapp.utils.json;


import com.alibaba.fastjson.JSONObject;

public class JsonToBean {
    public static Object changeObject(String JsonStr, Class<?> clazz){
        JSONObject jsonObject = JSONObject.parseObject(JsonStr);
        return jsonObject.toJavaObject(clazz);
    }
}
