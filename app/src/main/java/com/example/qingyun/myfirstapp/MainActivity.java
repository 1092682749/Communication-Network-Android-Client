package com.example.qingyun.myfirstapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qingyun.myfirstapp.service.RequestService;
import com.example.qingyun.myfirstapp.utils.HttpRequestor;
import com.example.qingyun.myfirstapp.utils.TimeClientHandler;

import org.zackratos.ultimatebar.UltimateBar;

import butterknife.Bind;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;

import javax.net.ssl.HttpsURLConnection;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Runnable {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static String host = "dyzhello.club";
    Integer port = 8080;
    Socket socket = null;
    EventLoopGroup workerGroup = null;
    Bootstrap b = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String path = "/sdcard/image/";
    MediaRecorder recorder = null;
    Button luyinBtn;
    Long recorderStartTime;
    Long recorderEndTime;
    public static String user = "123";
    Handler handler = new Handler();
    public static String receivename = "";
    public static String serizablePath = "/sdcard/ncc/user/";
    public static String recorderPath = "/sdcard/ncc/voice/";
    public static String appSavePath = "/sdcard/ncc/apkFile/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        luyinBtn = findViewById(R.id.luyin);

//        setTitle("Netty-Chat-Client");
//        setTitleColor(R.color.white);
        luyinBtn.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Toast.makeText(MainActivity.this,"录音开始" ,Toast.LENGTH_SHORT).show();
//                    luyinBtn.setBackgroundColor(Color.BLACK);
                    try {
                        recorderStartTime = System.currentTimeMillis();
                        audio(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    Toast.makeText(MainActivity.this,"录音结束" ,Toast.LENGTH_SHORT).show();
//                    luyinBtn.setBackgroundColor(Color.WHITE);
                    try{

                        recorderEndTime = System.currentTimeMillis();
                        if ((recorderEndTime - recorderStartTime) < 1000){
                            Thread.sleep(1000);
                        }
                        recorder.stop();
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"时间太短啦!",Toast.LENGTH_SHORT).show();
//                        recorder.release();
                    }

                }
                return false;
            }
        });
        Intent intent = new Intent(MainActivity.this, LoginActive.class);
//        Intent intent = new Intent(MainActivity.this, TestPopWindow.class);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.print("延时任务");
                        startActivity(intent);
                    }
                });
            }
        },3000);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            UltimateBar ultimateBar = new UltimateBar(this);
            ultimateBar.setHintBar();
        }
    }

    public void login(View view){
//        startService(new Intent(this,RequestService.class));
        Thread thread = new Thread(this);
        thread.start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                firstSuccess();
//            }
//        }).start();
//        Intent intent = new Intent(this,FriendListActivity.class);
//        startActivity(intent);
    }
    public void sendMessage(View view) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                nettyConnection();
            }
        });
//        net();
        thread.start();
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }
    public void net() throws IOException {
        Socket socket = new Socket("dyzhello.club",8080);
        //给服务端发送消息
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.write("hello" + "\r\n");
        printWriter.flush();
    }
    public void firstSuccess() {
        HttpRequestor httpRequestor = new HttpRequestor();
        String s = null;
        try {
//                    s = httpRequestor.doGet("https://dyzhello.club/ok/getMsgListByName?sendName=111&receiveName=qqq");
            URL url = new URL("https://dyzhello.club/ok/getMsgListByName?sendName=111&receiveName=qqq");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                s = reader.readLine();
            }
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // 未成功
    public void nettyConnection() {
        if (workerGroup == null){
            workerGroup= new NioEventLoopGroup();
            b = new Bootstrap();
        }
        try {
             // (y)
            b.group(workerGroup); // (e)
            b.channel(NioSocketChannel.class); // (ss)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (s)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (w)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }



    // 使用socket
    public void socketConnection() throws IOException {
        if (socket == null){
            socket = new Socket("192.168.0.181", 9000);
        }
        System.out.print(9000);
//e、获取输出流，向服务器端发送信息
        OutputStream os = socket.getOutputStream();//字节输出流
        PrintWriter pw =new PrintWriter(os);//将输出流包装成打印流
        pw.write("用户名：admin；密码：123");
        pw.flush();
        socket.shutdownOutput();
//ss、获取输入流，并读取服务器端的响应信息
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String info = null;
        while((info=br.readLine())!=null){
            System.out.println("我是客户端，服务器说："+info);
        }

//s、关闭资源
//        br.close();
//        is.close();
//        pw.close();
//        os.close();
//        socket.close();
    }
    public void okHttp() {
        
    }

    @Override
    public void run() {
        Intent intent = new Intent(this, ChatMainActivity.class);
        startActivity(intent);
    }
    public void imageCapture(View view){
//        Log.d(tag,"dispatchTakePictureIntent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void audio(View view) throws IOException {
        if (recorder == null) {
            recorder = new MediaRecorder();
        }
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            File filePath = new File("/sdcard/voice/");
            if (!filePath.exists()){
                filePath.mkdirs();
            }
            File saveFile = new File(filePath + "/voiceFile.mp3");
            if (!saveFile.exists()){
                saveFile.createNewFile();
            }
            recorder.setOutputFile(saveFile);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//            try {
//                recorder.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        recorder.prepare();
        recorder.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = findViewById(R.id.imageView);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            try {
                String imageStr = new String(baos.toByteArray(),"utf-8");
                File pf = new File(path);
                if (!pf.exists()){
                    pf.mkdirs();
                }
                String fileName = "saveTest.png";
//                BufferedOutputStream bos = new BufferedOutputStream(new ByteArrayOutputStream());
                File file = new File(path + fileName);
                if (!file.exists()){
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                // jdk8 工具类
                java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
                String baseStr = encoder.encodeToString(baos.toByteArray());
                java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
                byte[] changeByte = decoder.decode(baseStr);
                System.out.println(baseStr);
                bos.write(changeByte);
                bos.flush();
                bos.close();
                fos.close();

//                bos.write(imageStr.getBytes("utf-8"));
//                System.out.print(imageStr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
            mImageView.setImageBitmap(imageBitmap);
        }
    }
    public void none(View view){
        Toast.makeText(this,"时间太短啦！",Toast.LENGTH_SHORT).show();
    }
    public void auroraChat(View view){
        Intent intent = new Intent(this, AuroraActivity.class);
//        intent.setAction(Intent.ACTION_CALL);
        startActivity(intent);
    }
    public void toList(View view){
        Intent intent = new Intent(this,ListView.class);
        TextView t = findViewById(R.id.editText);
        user = t.getText().toString().trim();
        startActivity(intent);
    }
    public void toMyList(View view){
        Intent intent = new Intent(this,MyList.class);
        TextView t = findViewById(R.id.editText);
        user = t.getText().toString().trim();
        startActivity(intent);
    }

}
