package com.example.qingyun.myfirstapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qingyun.myfirstapp.utils.HttpRequestor;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActive extends AppCompatActivity {
    String response = "";
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_active);
    }
    public void toRegist(View view){
        Intent intent = new Intent(LoginActive.this, Regist.class);
        startActivity(intent);
    }
    public void login(View view) throws Exception {
        TextView usernameTextView = findViewById(R.id.login_username);
        TextView passwordTextView = findViewById(R.id.login_password);
        String username = usernameTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        HttpRequestor r = new HttpRequestor();

        new Thread(){
            @Override
            public void run() {
                try {
                    response = r.submitFrom("https://" + MainActivity.host + "/android/login","POST", map);
                    System.out.println(response);
                    com.alibaba.fastjson.JSONObject jb = JSON.parseObject(response);
                    if (jb.getString("msg").equals("登录成功!")){
                        MainActivity.user = username;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActive.this, jb.getString("msg"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActive.this, ListView.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActive.this, jb.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
