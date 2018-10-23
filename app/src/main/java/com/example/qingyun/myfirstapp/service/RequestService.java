package com.example.qingyun.myfirstapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestService extends Service {

    public RequestService() {
    }

    @Override
    public void onCreate() {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        doPost("https:/dyzhello.club/ok/getFriend",true,true,null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String doPost(String urlStr, Boolean doInput, Boolean doOutput,Object o) throws IOException {
        URL url = new URL(urlStr);
        PrintWriter writer = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(doInput);
        conn.setDoOutput(doOutput);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.connect();

        if (conn.getResponseCode() == 200){
            if (o != null){
                writer = new PrintWriter(conn.getOutputStream());
                String json = JSON.toJSONString(o);
                writer.print(json);
                writer.flush();
            }
            String line = "";
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        if (writer != null) {
            writer.close();
        }
        if (reader != null){
            reader.close();
        }
        System.out.print(result.toString());
        return result.toString();
    }
}
