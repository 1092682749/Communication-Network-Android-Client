package com.example.qingyun.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.qingyun.myfirstapp.utils.HttpRequestor;
import com.example.qingyun.myfirstapp.utils.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    String host = "192.168.0.181";
    Integer port = 8080;
    Socket socket = null;
    EventLoopGroup workerGroup = null;
    Bootstrap b = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void login(View view){
        Intent intent = new Intent(this, ChatMainActivity.class);
        startActivity(intent);
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
             // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)

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
//2、获取输出流，向服务器端发送信息
        OutputStream os = socket.getOutputStream();//字节输出流
        PrintWriter pw =new PrintWriter(os);//将输出流包装成打印流
        pw.write("用户名：admin；密码：123");
        pw.flush();
        socket.shutdownOutput();
//3、获取输入流，并读取服务器端的响应信息
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String info = null;
        while((info=br.readLine())!=null){
            System.out.println("我是客户端，服务器说："+info);
        }

//4、关闭资源
//        br.close();
//        is.close();
//        pw.close();
//        os.close();
//        socket.close();
    }
    public void okHttp() {
        
    }
}
