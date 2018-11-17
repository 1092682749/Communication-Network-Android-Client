package com.example.qingyun.myfirstapp;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.qingyun.myfirstapp.utils.NetWorkServer;
import com.example.qingyun.myfirstapp.utils.NetWorkServerListener;

import java.util.HashMap;

public class TestPopWindow extends AppCompatActivity {
    Boolean isJump = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pop_window);
        Button button = findViewById(R.id.pop);
        View view1 = getLayoutInflater().inflate(R.layout.layout, null);
        ImageView imageView = view1.findViewById(R.id.canJumpMic);
        imageView.getDrawable().setLevel(3500);
        view1.setFocusable(true);
        view1.setFocusableInTouchMode(true);
        Handler handler = new Handler();
        Long startTime = System.currentTimeMillis();
        TextView timeView = view1.findViewById(R.id.time_show);
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isJump){
                                int level = (int) (Math.random()*5000);
                                imageView.getDrawable().setLevel(3500 + level);
                                long st = (System.currentTimeMillis() - startTime);
                                String time = "" + st;
//                                (timeView).setText(time);
                            }
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        PopupWindow popupWindow = new PopupWindow(view1, 370, 370, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true); //设置点击menu以外其他地方以及返回键退出
        popupWindow.setOutsideTouchable(true);   //设置触摸外面时消失
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("dismiss");
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        popupWindow.showAtLocation(v,Gravity.CENTER,0,0);
                        System.out.println("show");
                        isJump = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        popupWindow.dismiss();
                        System.out.print("up");
                        isJump = false;
                        break;
                }
                return true;
            }
        });

//        new NetWorkServer().setNetWorkServerListener(new NetWorkServerListener() {
//                                                         @Override
//                                                         public void onSuccessed(Object response) {
//                                                             System.out.println(response);
//                                                         }
//
//                                                         @Override
//                                                         public void onFailed(Object message) {
//                                                            System.out.println("failed");
//                                                         }
//                                                     }
//        ).request("get","https://dyzhello.club/ok/getFriend", new HashMap());
    }
}
