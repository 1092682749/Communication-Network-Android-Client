package com.example.qingyun.myfirstapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qingyun.myfirstapp.pojo.User;
import com.example.qingyun.myfirstapp.utils.HttpRequestor;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Regist extends AppCompatActivity {
    String response;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        setTitleColor(R.color.white);
        setTitle("注册");
        Button button = findViewById(R.id.register_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    register();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void register() throws Exception {
        HttpRequestor requestor = new HttpRequestor();
        TextView usernameTextView = findViewById(R.id.register_username);
        TextView passwordTextView = findViewById(R.id.register_password);
        TextView nicknameTextView = findViewById(R.id.register_nickname);
        String username = usernameTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        String nickname = nicknameTextView.getText().toString();
        User user = new User();
        user.setUsername(username);
        user.setNickName(nickname);
        user.setPassword(password);
        HashMap<String, String> map = new HashMap<String,String>();
        map.put("username",username);
        map.put("nickName",nickname);
        map.put("password",password);
        // 开启网络请求线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 请求并将消息转换
                    response = requestor.doPost("https://" + MainActivity.host + "/ok/save", map);
                    System.out.println(response);
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                    if (jsonObject.getString("msg").equals("注册成功")) {
                        // 更新视图
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Regist.this, "注册成功!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Regist.this, LoginActive.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        handler.post( new Runnable() {
                                @Override
                                public void run() {
                                Toast.makeText(Regist.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
