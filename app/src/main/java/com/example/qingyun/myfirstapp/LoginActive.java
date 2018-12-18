package com.example.qingyun.myfirstapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qingyun.myfirstapp.pojo.User;
import com.example.qingyun.myfirstapp.utils.HttpRequestor;
import com.example.qingyun.myfirstapp.utils.NettyChatClient;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class LoginActive extends AppCompatActivity {
    String response = "";
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_active);
        setTitle("登录");
        setTitleColor(R.color.white);
        File userInfoPath = new File(MainActivity.serizablePath);
        ObjectInputStream ois = null;
        File loginUserOut = new File(MainActivity.serizablePath + "loginUser.out");
        if (loginUserOut.exists()) {
            try {
                /////////////////////////////
                ois = new ObjectInputStream(new FileInputStream(loginUserOut));
                User user = (User) ois.readObject();
                TextView usernameTextView = findViewById(R.id.login_username);
                TextView passwordTextView = findViewById(R.id.login_password);
                usernameTextView.setText(user.getUsername());
                passwordTextView.setText(user.getPassword());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                // 关闭输入流
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void toRegist(View view) {
        Intent intent = new Intent(LoginActive.this, Regist.class);
        startActivity(intent);
    }

    public void login(View view) throws Exception {
        ProgressDialog pd = ProgressDialog.show(this,"登录", "正在登录...", true, false);
        TextView usernameTextView = findViewById(R.id.login_username);
        TextView passwordTextView = findViewById(R.id.login_password);
        String username = usernameTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        HttpRequestor r = new HttpRequestor();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        new Thread() {
            @Override
            public void run() {
                try {
                    response = r.submitFrom("https://" + MainActivity.host + "/android/login", "POST", map);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pd.hide();
                        }
                    });
                    System.out.println(response);
                    com.alibaba.fastjson.JSONObject jb = JSON.parseObject(response);
                    if (jb.getString("msg").equals("登录成功!")) {
                        MainActivity.user = username;
                        // 序列化登录对象
                        File outPath = new File(MainActivity.serizablePath);
                        if (!outPath.exists()) {
                            outPath.mkdirs();
                        }
                        File outFile = new File(MainActivity.serizablePath + "loginUser.out");
                        if (!outFile.exists()) {
                            outFile.createNewFile();
                        }
                        ObjectOutputStream oos = null;
                        try {
                            // 打开输出流
                            oos = new ObjectOutputStream(new FileOutputStream(outFile));
                            oos.writeObject(user);
                            oos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (oos != null) {
                                oos.close();
                            }
                        }

                        // 建立连接
                        NettyChatClient.getInstance().connect();
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.action_menu, menu);
//        return true;
//    }
}
