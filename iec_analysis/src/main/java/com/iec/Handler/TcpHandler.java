package com.iec.Handler;

import com.iec.work.ruleComParse;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.regex.PatternSyntaxException;

/**
 * TcpHandler
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-09
 */
public class TcpHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当一个新的连接已经被建立时，channelActive(ChannelHandlerContext)将会被调用
        System.out.println("[[[[[[]]]]]] Client " + ctx.channel().remoteAddress() + " connected");
    }

    /**
     * 收到客户端消息，自动触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String uuid = ctx.channel().id().asLongText();
        /**
         * 将 msg 转为 Netty 的 ByteBuf 对象，类似 JDK 中的 java.nio.ByteBuffer，不过 ButeBuf 功能更强，更灵活
         */
        ByteBuf buf = (ByteBuf) msg;
        /**readableBytes：获取缓冲区可读字节数,然后创建字节数组
         * 从而避免了像 java.nio.ByteBuffer 时，只能盲目的创建特定大小的字节数组，比如 1024
         * */
        byte[] reg = new byte[buf.readableBytes()];
        /**readBytes：将缓冲区字节数组复制到新建的 byte 数组中
         * 然后将字节数组转为字符串
         * */
        buf.readBytes(reg);
        buf.clear();
        /**回复消息
         * copiedBuffer：创建一个新的缓冲区，内容为里面的参数
         * 通过 ChannelHandlerContext 的 write 方法将消息异步发送给客户端
         * */
        String respMsg = "message parse err!";
        writectx(ctx, reg, respMsg);

    }

    private void writectx(ChannelHandlerContext ctx, byte[] reg, String respMsg) {
        try {
//            String body = HexBin.encode(reg);
//            System.out.println("====" + Thread.currentThread().getName() + ",The server receive order : " + body);
//            String analysis = Analysis.analysis(body.replaceAll(" ", ""));
//            System.out.println(analysis);

            respMsg = ruleComParse.I.parseRuleCom(ctx, reg);
        } catch (PatternSyntaxException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        System.out.println("----" + Thread.currentThread().getName() + ",Command message response : " + respMsg);
        if (!"".equals(respMsg)&&respMsg!=null) {
            ByteBuf respByteBuf = Unpooled.copiedBuffer(HexBin.decode(respMsg));
            ctx.write(respByteBuf);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /**flush：将消息发送队列中的消息写入到 SocketChannel 中发送给对方，为了频繁的唤醒 Selector 进行消息发送
         * Netty 的 write 方法并不直接将消息写如 SocketChannel 中，调用 write 只是把待发送的消息放到发送缓存数组中，再通过调用 flush
         * 方法，将发送缓冲区的消息全部写入到 SocketChannel 中
         * */
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /**当发生异常时，关闭 ChannelHandlerContext，释放和它相关联的句柄等资源 */
        ctx.close();
    }
}
