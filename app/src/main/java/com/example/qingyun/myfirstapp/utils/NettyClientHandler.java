package com.example.qingyun.myfirstapp.utils;

import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;
import com.example.qingyun.myfirstapp.utils.json.JsonToBean;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("服务器消息：" + msg.toString());
        ChatMsgRecord chatMsgRecord = (ChatMsgRecord)JsonToBean.changeObject(msg.toString(),ChatMsgRecord.class);
        CacheMessage.setMessage(chatMsgRecord);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
