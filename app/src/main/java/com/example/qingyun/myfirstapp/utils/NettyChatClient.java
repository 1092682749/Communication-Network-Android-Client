package com.example.qingyun.myfirstapp.utils;

import android.app.Service;

import com.alibaba.fastjson.JSON;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;

import org.json.JSONObject;

import javax.net.ssl.SSLEngine;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.FutureListener;

public enum  NettyChatClient {
    NETTY_CHAT_CLIENT();
    String host = "119.29.s.88";
    int port = 8000;
    private EventLoopGroup group;
    private Bootstrap b;
    private ChannelFuture cf ;
    private NettyChatClient(){
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new StringDecoder());
//                        ch.pipeline().addLast(new StringEncoder());
//                        ch.pipeline().addLast(new ObjectEncoder());
//                        ch.pipeline().addLast(new ObjectDecoder(new ClassResolver() {
//                            @Override
//                            public Class<?> resolve(String className) throws ClassNotFoundException {
//                                return Class.forName(className);
//                            }
//                        }));
//                        engine.setUseClientMode(true);
//                        ch.pipeline().addLast("ssl", new SslHandler(engine));
                        ChannelPipeline p = ch.pipeline();
                        SslContext sslCtx = SslContextBuilder.forClient().build();
                        p.addLast("ssl", sslCtx.newHandler(ch.alloc(), host, port));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    public void connect(){
        try {
            this.cf = b.connect(host, port).sync();
            this.cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("连接成功");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChannelFuture getChannelFuture() {
        //如果管道没有被开启或者被关闭了，那么重连
        if (this.cf == null) {
            this.connect();
        }
        if (!this.cf.channel().isActive()) {
            this.connect();
        }
        return this.cf;
    }
    // 暂时以String代替消息对象
    public void write(String msg) {
        System.out.println("send..." + msg);
        ChatMsgRecord chatMsgRecord = new ChatMsgRecord();
        chatMsgRecord.setContent(msg);
        chatMsgRecord.setSendname("client1");
        chatMsgRecord.setReceivename("server");
        String json = JSON.toJSONString(chatMsgRecord);
        System.out.println(json);
        ChannelFuture channelFuture = getChannelFuture();
        channelFuture.channel().writeAndFlush(json);
    }
    public static NettyChatClient getInstance(){
        return NETTY_CHAT_CLIENT;
    }
}
